package com.dws.account.service.impl;

import com.dws.account.entity.AccountEntity;
import com.dws.account.exception.AccountNotFoundException;
import com.dws.account.exception.DuplicateAccountException;
import com.dws.account.exception.InsufficientBalanceException;
import com.dws.account.model.Account;
import com.dws.account.model.TransferRequest;
import com.dws.account.repository.AccountRepository;
import com.dws.account.service.AccountService;
import com.dws.account.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.dws.account.util.AccountConstants.AMOUNT_CREDITED;
import static com.dws.account.util.AccountConstants.AMOUNT_DEBITED;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final Lock lock = new ReentrantLock();
    private final NotificationService notificationService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public void createAccount(Account account) {
        log.info(" AccountServiceImpl::createAccount :: Creating account {}", account);
        accountRepository.findById(account.getId()).ifPresent((acc) -> {
            throw new DuplicateAccountException("Account with ID " + account.getId() + " already exists");
        });
        AccountEntity accountEntity = modelMapper.map(account, AccountEntity.class);
        if (Objects.nonNull(accountEntity))
            accountRepository.saveAndFlush(accountEntity);
    }

    public Account getAccountDetails(Long accountId) {
        log.info(" AccountServiceImpl::getAccountDetails :: Account {}", accountId);
        AccountEntity accountEntity = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));
        return Objects.nonNull(accountEntity) ? modelMapper.map(accountEntity, Account.class) : Account.builder().build();
    }

    @Override
    @Transactional
    public void transferAmount(TransferRequest transferRequest) {
        log.info(" AccountServiceImpl :: transferAmount ::  Transfer  Amount {} from Account Id {}", transferRequest.getAmount(), transferRequest.getAccountIdTo());


        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                try {
                    AccountEntity accountFrom = accountRepository.findById(transferRequest.getAccountIdFrom())
                            .orElseThrow(() -> new AccountNotFoundException("Account with ID " + transferRequest.getAccountIdFrom() + " not found"));
                    AccountEntity accountTo = accountRepository.findById(transferRequest.getAccountIdTo())
                            .orElseThrow(() -> new AccountNotFoundException("Account with ID " + transferRequest.getAccountIdTo() + " not found"));

                    if (accountFrom.getBalance().compareTo(transferRequest.getAmount()) < 0) {
                        throw new InsufficientBalanceException("Insufficient balance in account with ID " + transferRequest.getAccountIdFrom());
                    }

                    accountFrom.setBalance(accountFrom.getBalance().subtract(transferRequest.getAmount()));
                    accountTo.setBalance(accountTo.getBalance().add(transferRequest.getAmount()));

                    accountRepository.save(accountFrom);
                    accountRepository.save(accountTo);
                    notificationService.sendNotification(transferRequest.getAccountIdFrom(), AMOUNT_DEBITED);
                    notificationService.sendNotification(transferRequest.getAccountIdTo(), AMOUNT_CREDITED);


                } finally {
                    lock.unlock();
                }
            } else {
                throw new RuntimeException("Unable to acquire lock within timeout period");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while acquiring lock");
        }
    }
}
