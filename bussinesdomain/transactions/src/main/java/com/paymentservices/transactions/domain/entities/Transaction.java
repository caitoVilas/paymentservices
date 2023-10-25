package com.paymentservices.transactions.domain.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private String ibanAccount;
    private LocalDateTime date;
    private Double amount;
    private boolean fee;
    private String dedcription;
    private String status;
    private String channel;
}
