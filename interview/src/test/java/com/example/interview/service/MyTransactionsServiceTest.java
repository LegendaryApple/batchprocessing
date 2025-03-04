package com.example.interview.service;

import com.example.interview.entity.MyTransactions;
import com.example.interview.repository.MyTransactionsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootTest
public class MyTransactionsServiceTest {

    @Autowired
    private MyTransactionsRepository transactionRepository;

    @Test
    void testOptimisticLocking() throws InterruptedException, ExecutionException {
        // Fetch the same transaction
        MyTransactions trx1 = transactionRepository.findById(1L).orElseThrow();
        MyTransactions trx2 = transactionRepository.findById(1L).orElseThrow();

        // Modify both instances
        trx1.setDescription("First update");
        trx2.setDescription("Second update");

        // Start first update in a separate thread
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> future1 = executor.submit(() -> {
            transactionRepository.save(trx1);
        });

        // Start second update (should fail)
        Future<?> future2 = executor.submit(() -> {
            Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                transactionRepository.save(trx2);
            });
        });

        // Wait for both threads to complete
        future1.get();
        future2.get();

        executor.shutdown();
    }
}
