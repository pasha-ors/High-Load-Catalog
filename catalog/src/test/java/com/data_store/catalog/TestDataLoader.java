package com.data_store.catalog;

import com.data_store.catalog.model.Product;
import com.data_store.catalog.model.ProductVariant;
import com.data_store.catalog.repository.ProductRepository;
import com.data_store.catalog.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class TestDataLoader {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Test
    void saveAndLoadProduct(){
        Product product = new Product();
        product.setName("IPhone");
        product.setCreatedAt(Instant.now());

        productRepository.save(product);

        ProductVariant variant = new ProductVariant();
        variant.setProductId(product.getId());
        variant.setPrice(new BigDecimal("999.99"));
        variantRepository.save(variant);

        Optional<Product> loadProduct = productRepository.findById(product.getId());
        Optional<ProductVariant> loadVariant = variantRepository.findById(variant.getId());

        assertTrue(loadProduct.isPresent());
        assertTrue(loadVariant.isPresent());

        assertEquals(product.getName(), loadProduct.get().getName());
        assertEquals(product.getCreatedAt(), loadProduct.get().getCreatedAt());

        assertEquals(variant.getProductId(), loadVariant.get().getProductId());
        assertEquals(variant.getPrice(), loadVariant.get().getPrice());
    }
}
