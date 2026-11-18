package com.example.banking_application.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    private double amount;

    private LocalDateTime date;

    private String type;

    @ManyToOne
    @JoinColumn(name = "a_id")
    private Account account;
}
