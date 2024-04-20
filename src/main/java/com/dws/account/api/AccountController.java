package com.dws.account.api;


import com.dws.account.model.Account;
import com.dws.account.model.TransferRequest;
import com.dws.account.service.AccountService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dws.account.util.AccountConstants.ACCOUNT_CREATED;

@RestController
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
            accountService.createAccount(account);
            return ResponseEntity.ok(ACCOUNT_CREATED);

    }

    @GetMapping("/getAccountDetails/{accountId}")
    public ResponseEntity<Account> getAccountDetails(@PathVariable Long accountId) {
            Account account = accountService.getAccountDetails(accountId);
            return ResponseEntity.ok(account);
    }

    @PostMapping("/transferAmount")
    public ResponseEntity<String> transferAmount(@RequestBody TransferRequest transferRequest) {
            accountService.transferAmount(transferRequest);
            return ResponseEntity.ok("Amount transferred successfully");
    }
}

