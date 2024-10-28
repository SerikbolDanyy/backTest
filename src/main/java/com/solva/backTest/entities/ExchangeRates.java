package com.solva.backTest.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ExchangeRates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currencyFrom;
    private String currencyTo;
    private double rate;
    private LocalDateTime date;

    public ExchangeRates() {
        this.date = LocalDateTime.now();
    }

    public ExchangeRates(String currencyFrom, String currencyTo, double rate) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
        this.date = LocalDateTime.now();
    }
}
