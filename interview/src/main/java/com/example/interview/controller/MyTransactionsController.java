package com.example.interview.controller;

import com.example.interview.entity.MyTransactions;
import com.example.interview.params.TransactionUpdateParam;
import com.example.interview.service.MyTransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RestController
@RequestMapping("/myTransactions")
public class MyTransactionsController {
    private final MyTransactionsService myTransactionsService;

    public MyTransactionsController(MyTransactionsService myTransactionsService) {
        this.myTransactionsService = myTransactionsService;
    }

    // ✅ Search transactions with pagination
    @GetMapping("/search")
    public Page<MyTransactions> searchTransactions(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String description,
            Pageable pageable) {
        return myTransactionsService.searchTransactions(customerId, accountNumber, description, pageable);
    }

    // ✅ Update a transaction by ID
    @PutMapping("/updateTransaction")
    public ResponseEntity<Boolean> updateTransaction(
            @RequestBody TransactionUpdateParam transactionUpdateParam) {
        try {
            MyTransactions transaction = myTransactionsService.updateTransaction(transactionUpdateParam);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
