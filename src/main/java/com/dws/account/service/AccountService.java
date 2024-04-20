package com.dws.account.service;

import com.dws.account.model.Account;
import com.dws.account.model.TransferRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {


    public void createAccount(Account account);

    public Account getAccountDetails(Long accountId);

    public void transferAmount(TransferRequest transferRequest);
}
