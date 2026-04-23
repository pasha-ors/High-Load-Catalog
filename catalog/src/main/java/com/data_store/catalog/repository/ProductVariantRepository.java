package com.data_store.catalog.repository;

import com.data_store.catalog.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    void deleteByProductId(Long productId);
    int countByProductId(Long productId);
}
