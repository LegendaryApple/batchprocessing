package com.example.interview.service;

import com.example.interview.entity.MyTransactions;
import com.example.interview.params.TransactionUpdateParam;
import com.example.interview.repository.MyTransactionsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyTransactionsService {

    @Autowired
    private MyTransactionsRepository repository;

    public Page<MyTransactions> searchTransactions(Long customerId, String accountNumber, String description, Pageable pageable) {
        return repository.searchTransactions(customerId, accountNumber, description, pageable);
    }

    public Optional<MyTransactions> getTransactionById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public MyTransactions updateTransaction(TransactionUpdateParam transactionUpdateParam) {
        MyTransactions myTransactions = repository.findById(transactionUpdateParam.id())
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionUpdateParam.id()));
        myTransactions.setDescription(transactionUpdateParam.description());
        return repository.save(myTransactions);
    }
}

