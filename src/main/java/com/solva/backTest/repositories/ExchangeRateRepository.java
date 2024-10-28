package com.solva.backTest.repositories;

import com.solva.backTest.entities.ExchangeRates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRates, Long> {
    Optional<ExchangeRates> findTopByCurrencyFromAndCurrencyToOrderByDateDesc(
            String currencyFrom, String currencyTo);
}
