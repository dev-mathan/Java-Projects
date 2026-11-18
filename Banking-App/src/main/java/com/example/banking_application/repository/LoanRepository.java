package com.example.banking_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banking_application.entity.Loan;

public interface LoanRepository
        extends JpaRepository<Loan, Long> {

    List<Loan> findByUserUid(Long uid);
}
