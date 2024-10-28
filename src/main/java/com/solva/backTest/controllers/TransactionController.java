package com.solva.backTest.controllers;
import com.solva.backTest.entities.Transaction;
import com.solva.backTest.services.TransactionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }
    @GetMapping("/exceeded")
    public List<Transaction> getOverLimitTransactions(){
        return transactionService.getTransactionsOverLimit();
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction){
        return transactionService.saveTransaction(transaction);
    }
}
