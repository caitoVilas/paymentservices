package com.paymentservices.transactions.domain.repositories;

import com.paymentservices.transactions.domain.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByIbanAccount(String ibanAccount);
}
