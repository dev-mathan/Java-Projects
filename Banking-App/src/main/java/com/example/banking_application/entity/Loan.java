package com.example.banking_application.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    private double amount;

    private LocalDateTime applyDate;
    private LocalDateTime approvalData;
    private LocalDateTime disbursementDate;

    private double balance;
    private String status;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;
}
