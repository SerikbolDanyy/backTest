package com.solva.backTest.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private double limitSum;
    private LocalDateTime dateTime;
    private String currency;

    public Limit() {
        this.dateTime = LocalDateTime.now();
    }
}
