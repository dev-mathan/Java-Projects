package com.example.banking_application.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.banking_application.entity.Transaction;
import com.example.banking_application.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/account/{accountId}")
    public ResponseEntity<Transaction> createTransaction(
            @PathVariable Long accountId,
            @RequestBody Transaction transaction) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(
                        accountId, transaction));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(
                transactionService.getAllTransactions());
    }

    @GetMapping("/{tid}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable Long tid) {
        return ResponseEntity.ok(
                transactionService.getTransactionById(tid));
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<Page<Transaction>> getUserTransactions(
            @PathVariable Long uid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(
                transactionService.getTransactionsByUser(
                        uid, page, size));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getAccountTransactions(
            @PathVariable Long accountId) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByAccount(accountId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Transaction>> getByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {

        return ResponseEntity.ok(
                transactionService.getTransactionsByDateRange(
                        start, end));
    }

    @GetMapping("/amount-range")
    public ResponseEntity<List<Transaction>> getByAmountRange(
            @RequestParam double min,
            @RequestParam double max) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByAmountRange(
                        min, max));
    }

    @DeleteMapping("/{tid}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long tid) {
        transactionService.deleteTransaction(tid);
        return ResponseEntity.noContent().build();
    }
}
