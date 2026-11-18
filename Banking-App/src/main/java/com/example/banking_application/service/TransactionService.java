package com.example.banking_application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.banking_application.entity.Account;
import com.example.banking_application.entity.Transaction;
import com.example.banking_application.exception.ResourceNotFountException;
import com.example.banking_application.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Transaction createTransaction(
            Long accountId, Transaction transaction) {

        Account account = accountService.getAccountById(accountId);

        transaction.setAccount(account);

        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long tid) {
        return transactionRepository.findById(tid)
                .orElseThrow(() ->
                    new ResourceNotFountException(
                        "Transaction not found: " + tid));
    }

    public Page<Transaction> getTransactionsByUser(
            Long uid, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository
                .findByAccountUserUid(uid, pageable);
    }

    public List<Transaction> getTransactionsByAccount(
            Long accountId) {
        return transactionRepository
                .findByAccountAId(accountId);
    }

    public List<Transaction> getTransactionsByDateRange(
            LocalDateTime start, LocalDateTime end) {
        return transactionRepository
                .findByDateBetween(start, end);
    }

    public List<Transaction> getTransactionsByAmountRange(
            double min, double max) {
        return transactionRepository
                .findByAmountBetween(min, max);
    }

    public void deleteTransaction(Long tid) {
        Transaction transaction = getTransactionById(tid);
        transactionRepository.delete(transaction);
    }
}
