package com.paymentservices.transactions.api.controllers;

import com.paymentservices.transactions.domain.entities.Transaction;
import com.paymentservices.transactions.domain.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionRepository transactionRepository;

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction){
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionRepository.save(transaction));
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable Long id){
        return transactionRepository.findById(id).orElse(null);
    }

    @GetMapping
    public List<Transaction> getAll(){
        return transactionRepository.findAll();
    }

    @GetMapping("/customers/transactions")
    public List<Transaction> get(@RequestParam String ibanAccount){
        return transactionRepository.findByIbanAccount(ibanAccount);
    }
}
