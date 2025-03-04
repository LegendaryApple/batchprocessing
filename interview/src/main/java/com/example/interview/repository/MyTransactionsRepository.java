package com.example.interview.repository;

import com.example.interview.entity.MyTransactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyTransactionsRepository extends JpaRepository<MyTransactions, Long> {

    @Query("SELECT t FROM MyTransactions t WHERE " +
            "(:customerId IS NULL OR t.customerId = :customerId) AND " +
            "(:accountNumber IS NULL OR t.accountNumber LIKE %:accountNumber%) AND " +
            "(:description IS NULL OR t.description LIKE %:description%)")
    Page<MyTransactions> searchTransactions(
            @Param("customerId") Long customerId,
            @Param("accountNumber") String accountNumber,
            @Param("description") String description,
            Pageable pageable);
}
