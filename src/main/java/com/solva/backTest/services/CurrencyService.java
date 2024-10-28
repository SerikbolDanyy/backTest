package com.solva.backTest.services;
import com.solva.backTest.entities.ExchangeRates;
import com.solva.backTest.repositories.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    private final RestTemplate restTemplate;
    private static final String API_URL = "https://api.twelvedata.com/time_series";
    private static final String API_KEY = "88b4186504914f9a925f0cefe0ab111a";

    private final ExchangeRateRepository exchangeRateRepository;

    public CurrencyService(RestTemplate restTemplate, ExchangeRateRepository exchangeRateRepository) {
        this.restTemplate = restTemplate;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public double getExchangeRate(String from, String to) {
        String url = String.format("%s?symbol=%s/%s&interval=1day&apikey=%s", API_URL, from, to, API_KEY);
        try {
            var response = restTemplate.getForObject(url, CurrencyResponse.class);
            double rate = response.getValues().get(0).getClose();
            saveExchangeRateToDatabase(from, to, rate);
            return rate;
        } catch (Exception e) {
            return getLastRateFromDatabase(from, to)
                    .orElseThrow(() -> new RuntimeException("Курс валют недоступен"));
        }
    }



    public double convertToUSD(String currency, double amount) {
        if (currency.equalsIgnoreCase("USD")) {
            return amount;
        }
        double rate = getExchangeRate(currency, "USD");
        return amount / rate;
    }

    private void saveExchangeRateToDatabase(String from, String to, double rate) {
        ExchangeRates exchangeRate = new ExchangeRates();
        exchangeRate.setCurrencyFrom(from);
        exchangeRate.setCurrencyTo(to);
        exchangeRate.setRate(rate);
        exchangeRateRepository.save(exchangeRate);
    }

    private Optional<Double> getLastRateFromDatabase(String from, String to) {
        return exchangeRateRepository.findTopByCurrencyFromAndCurrencyToOrderByDateDesc(from, to)
                .map(ExchangeRates::getRate);
    }

    static class CurrencyResponse {
        private List<Value> values;

        public List<Value> getValues() {
            return values;
        }

        public void setValues(List<Value> values) {
            this.values = values;
        }

        // Вложенный класс Value для курса валют
        static class Value {
            private double close;

            public double getClose() {
                return close;
            }

            public void setClose(double close) {
                this.close = close;
            }
        }
    }
}
