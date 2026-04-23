package com.data_store.catalog.dto;

public record UpdateProductRequest(
        String name,
        Long categoryId
) {}
