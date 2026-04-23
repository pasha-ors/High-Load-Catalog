package com.data_store.catalog.dto;

import java.math.BigDecimal;

public record ProductPreviewDTO(
        Long id,
        String name,
        String categoryName,
        java.math.BigDecimal price
) {}
