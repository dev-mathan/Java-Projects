package com.example.banking_application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.banking_application.entity.Loan;
import com.example.banking_application.service.LoanService;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/{uid}")
    public ResponseEntity<Loan> createLoan(
            @PathVariable Long uid,
            @RequestBody Loan loan) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.createLoan(uid, loan));
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> getLoan(
            @PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.getLoanById(loanId));
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<List<Loan>> getUserLoans(
            @PathVariable Long uid) {
        return ResponseEntity.ok(loanService.getLoansByUser(uid));
    }

    @PatchMapping("/{loanId}/status")
    public ResponseEntity<Loan> updateStatus(
            @PathVariable Long loanId,
            @RequestParam String status) {
        return ResponseEntity.ok(
                loanService.updateLoanStatus(loanId, status));
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteLoan(
            @PathVariable Long loanId) {
        loanService.deleteLoan(loanId);
        return ResponseEntity.noContent().build();
    }
}
