package com.example.banking_application.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banking_application.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccountUserUid(
            Long uid, Pageable pageable);

    // Explicit query: the Account id field is named "a_id" (with an underscore),
    // which Spring Data's method-name parsing cannot resolve from "AccountAId".
    @Query("select t from Transaction t where t.account.a_id = :accountId")
    List<Transaction> findByAccountAId(
            @Param("accountId") Long accountId);

    List<Transaction> findByDateBetween(
            LocalDateTime start,
            LocalDateTime end);

    List<Transaction> findByAmountBetween(
            double minAmount,
            double maxAmount);
}
