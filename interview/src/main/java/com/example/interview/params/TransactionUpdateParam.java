package com.example.interview.params;

public record TransactionUpdateParam(
        Long id,
        String description,
        Integer version
) {};