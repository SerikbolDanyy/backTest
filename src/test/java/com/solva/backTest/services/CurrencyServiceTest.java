package com.solva.backTest.services;
import com.solva.backTest.entities.ExchangeRates;
import com.solva.backTest.repositories.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {

    private ExchangeRateRepository repository;
    private RestTemplate restTemplate;
    private CurrencyService currencyService;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(ExchangeRateRepository.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        currencyService = new CurrencyService(restTemplate, repository);
    }

    @Test
    public void testGetExchangeRateFromApiAndSaveToDatabase() {
        CurrencyService.CurrencyResponse.Value value = new CurrencyService.CurrencyResponse.Value();
        value.setClose(450.0);

        CurrencyService.CurrencyResponse response = new CurrencyService.CurrencyResponse();
        response.setValues(List.of(value));

        Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(CurrencyService.CurrencyResponse.class)))
                .thenReturn(response);

        double rate = currencyService.getExchangeRate("KZT", "USD");

        assertThat(rate).isEqualTo(450.0);

        Mockito.verify(repository).save(Mockito.any(ExchangeRates.class));
    }

    @Test
    public void testFallbackToDatabaseWhenApiFails() {
        Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(CurrencyService.CurrencyResponse.class)))
                .thenThrow(new RuntimeException("API недоступен"));

        Mockito.when(repository.findTopByCurrencyFromAndCurrencyToOrderByDateDesc("KZT", "USD"))
                .thenReturn(Optional.of(new ExchangeRates("KZT", "USD", 450.0)));

        double rate = currencyService.getExchangeRate("KZT", "USD");

        assertThat(rate).isEqualTo(450.0);
    }

    @Test
    public void testFallbackToDatabaseThrowsExceptionWhenNoRateAvailable() {
        Mockito.when(repository.findTopByCurrencyFromAndCurrencyToOrderByDateDesc("KZT", "USD"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> currencyService.getExchangeRate("KZT", "USD"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Курс валют недоступен");
    }

    @Test
    public void testConvertToUSD() {
        Mockito.when(repository.findTopByCurrencyFromAndCurrencyToOrderByDateDesc("KZT", "USD"))
                .thenReturn(Optional.of(new ExchangeRates("KZT", "USD", 450.0)));

        double amountInKZT = 9000.0;
        double convertedAmount = currencyService.convertToUSD("KZT", amountInKZT);

        assertThat(convertedAmount).isEqualTo(20.0);
    }
}
