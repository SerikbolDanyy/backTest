package com.solva.backTest.services;

import com.solva.backTest.entities.Transaction;
import com.solva.backTest.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private LimitService limitService;
    private CurrencyService currencyService;
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        limitService = Mockito.mock(LimitService.class);
        currencyService = Mockito.mock(CurrencyService.class);
        transactionService = new TransactionService(transactionRepository, limitService, currencyService);
    }

    @Test
    public void testSaveTransactionWithExceededLimit() {
        Mockito.when(currencyService.convertToUSD("KZT", 50000.0)).thenReturn(100.0);

        Mockito.when(limitService.isLimitExceeded(100.0)).thenReturn(true);

        Transaction transaction = new Transaction();
        transaction.setCurrency("KZT");
        transaction.setSum(50000.0);

        Mockito.when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.isLimitExceeded()).isTrue();
    }

    @Test
    public void testGetExceededTransactions() {
        Mockito.when(transactionRepository.findByLimitExceededTrue()).thenReturn(List.of(new Transaction()));

        List<Transaction> exceededTransactions = transactionService.getTransactionsOverLimit();

        assertThat(exceededTransactions).isNotEmpty();
    }
}