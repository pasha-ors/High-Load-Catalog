package com.data_store.catalog.dto;

import java.math.BigDecimal;

public record ProductPreviewDB(
        Long id,
        String name,
        Long categoryId,
        BigDecimal price
) {
}
