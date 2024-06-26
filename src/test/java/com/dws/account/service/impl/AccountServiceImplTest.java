package com.dws.account.service.impl;

import com.dws.account.entity.AccountEntity;
import com.dws.account.exception.DuplicateAccountException;
import com.dws.account.exception.AccountNotFoundException;
import com.dws.account.model.Account;
import com.dws.account.model.TransferRequest;
import com.dws.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private EmailNotificationService notificationService;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void createAccount_Success() {
        AccountEntity account = new AccountEntity();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1000.00));
        when(modelMapper.map(account,Account.class)).thenReturn(Account.builder().id(1L).balance(BigDecimal.valueOf(1000.0)).build());
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(accountRepository.saveAndFlush(account)).thenReturn(account);
        accountService.createAccount(Account.builder().id(1L).balance(new BigDecimal("5000.0")).build());
    }

    @Test
    void createAccount_AccountAlreadyExists() {
        AccountEntity account = new AccountEntity();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1000.00));

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        DuplicateAccountException exception = assertThrows(DuplicateAccountException.class, () -> accountService.createAccount(Account.builder().id(1L).balance(BigDecimal.valueOf(1)).build()));
        assertEquals("Account with ID 1 already exists", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void getAccountDetails_Success() {
        AccountEntity account = new AccountEntity();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1000.00));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountDetails(1L);
        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getBalance(), result.getBalance());
    }

    @Test
    void getAccountDetails_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> accountService.getAccountDetails(1L));
        assertEquals("Account with ID 1 not found", exception.getMessage());
    }
    @Test
    public void testTransferAmountSuccess() {
        TransferRequest transferRequest = new TransferRequest(2L, 1L, new BigDecimal(1000.0));
        AccountEntity account1=new AccountEntity();
        account1.setBalance(new BigDecimal(6000.0));
        account1.setId(1L);
        AccountEntity account2=new AccountEntity();
        account2.setBalance(new BigDecimal(3000.0));
        account2.setId(2L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));
        doNothing().when(notificationService).sendNotification(anyLong(),anyString());

        accountService.transferAmount(transferRequest);


        assertEquals(new BigDecimal(2000.0),account2.getBalance());
        assertEquals(new BigDecimal(7000.0),account1.getBalance());
    }


}
