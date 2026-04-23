package com.data_store.catalog.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        String name,
        Long categoryId,
        List<BigDecimal> prices
) { }
