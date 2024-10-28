package com.solva.backTest.repositories;
import com.solva.backTest.entities.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitRepository extends JpaRepository<Limit, Long> {
    Limit findTopByOrderByDateTimeDesc();
}
