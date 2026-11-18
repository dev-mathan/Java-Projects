package com.example.banking_application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.banking_application.entity.Loan;
import com.example.banking_application.entity.User;
import com.example.banking_application.exception.ResourceNotFountException;
import com.example.banking_application.repository.LoanRepository;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserService userService;

    public LoanService(
            LoanRepository loanRepository,
            UserService userService) {
        this.loanRepository = loanRepository;
        this.userService = userService;
    }

    public Loan createLoan(Long uid, Loan loan) {
        User user = userService.getUserById(uid);
        user.addloan(loan);

        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() ->
                    new ResourceNotFountException(
                        "Loan not found: " + loanId));
    }

    public List<Loan> getLoansByUser(Long uid) {
        userService.getUserById(uid);
        return loanRepository.findByUserUid(uid);
    }

    public Loan updateLoanStatus(Long loanId, String status) {
        Loan loan = getLoanById(loanId);
        loan.setStatus(status);
        return loanRepository.save(loan);
    }

    public void deleteLoan(Long loanId) {
        Loan loan = getLoanById(loanId);
        loanRepository.delete(loan);
    }
}
