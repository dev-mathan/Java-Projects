package com.example.banking_application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.banking_application.entity.Account;
import com.example.banking_application.entity.User;
import com.example.banking_application.exception.ResourceNotFountException;
import com.example.banking_application.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;

    public AccountService(
            AccountRepository accountRepository,
            UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    @Transactional
    public Account createAccount(Long uid, Account account) {
        User user = userService.getUserById(uid);

        user.addAccount(account);

        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                    new ResourceNotFountException(
                        "Account not found: " + accountId));
    }

    public List<Account> getAccountsByUser(Long uid) {
        userService.getUserById(uid);
        return accountRepository.findByUserUid(uid);
    }

    @Transactional
    public void deleteAccount(Long uid, Long accountId) {
        User user = userService.getUserById(uid);

        Account account = getAccountById(accountId);

        if (account.getUser() == null
                || !account.getUser().getUid().equals(uid)) {
            throw new ResourceNotFountException(
                    "Account does not belong to this user");
        }

        user.removeAccount(account);
    }
}
