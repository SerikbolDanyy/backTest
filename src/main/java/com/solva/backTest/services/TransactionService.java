package com.solva.backTest.services;
import com.solva.backTest.entities.Limit;
import com.solva.backTest.entities.Transaction;
import com.solva.backTest.repositories.LimitRepository;
import com.solva.backTest.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final LimitService limitService;
    private final CurrencyService currencyService;


    public TransactionService(TransactionRepository transactionRepository, LimitService limitService, CurrencyService currencyService){
        this.transactionRepository = transactionRepository;
        this.limitService = limitService;
        this.currencyService = currencyService;
    }

    public Transaction saveTransaction(Transaction transaction) {
        double sumInUSD = currencyService.convertToUSD(transaction.getCurrency(), transaction.getSum());
        boolean exceeded = limitService.isLimitExceeded(sumInUSD);
        transaction.setLimitExceeded(exceeded);
        transaction.setDateTime(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsOverLimit(){
       return transactionRepository.findByLimitExceededTrue();
   }

}
