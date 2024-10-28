package com.solva.backTest.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountFrom;
    private String accountTo;
    private String currency;
    private double sum;
    private String category;
    private LocalDateTime dateTime;
    private boolean limitExceeded;
}
