package com.example.banking_application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.banking_application.entity.Account;
import com.example.banking_application.service.AccountService;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{uid}")
    public ResponseEntity<Account> createAccount(
            @PathVariable Long uid,
            @RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(uid, account));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(
            @PathVariable Long accountId) {
        return ResponseEntity.ok(
                accountService.getAccountById(accountId));
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<List<Account>> getUserAccounts(
            @PathVariable Long uid) {
        return ResponseEntity.ok(
                accountService.getAccountsByUser(uid));
    }

    @DeleteMapping("/user/{uid}/account/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long uid,
            @PathVariable Long accountId) {
        accountService.deleteAccount(uid, accountId);
        return ResponseEntity.noContent().build();
    }
}
