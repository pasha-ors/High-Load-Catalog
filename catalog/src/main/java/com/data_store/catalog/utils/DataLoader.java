package com.data_store.catalog.utils;

import com.data_store.catalog.model.Category;
import com.data_store.catalog.model.Product;
import com.data_store.catalog.model.ProductVariant;
import com.data_store.catalog.repository.CategoryRepository;
import com.data_store.catalog.repository.ProductRepository;
import com.data_store.catalog.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner{
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final ProductVariantRepository variantRepo;

    @Override
    public void run(String... args) throws Exception {

        categoryRepo.deleteAll();
        productRepo.deleteAll();
        variantRepo.deleteAll();

        // --- Categories ---
        Category phones = new Category();
        phones.setName("Phones");

        Category laptops = new Category();
        laptops.setName("Laptops");

        categoryRepo.saveAll(List.of(phones, laptops));

        // --- Products ---
        Product iphone = new Product();
        iphone.setName("iPhone");
        iphone.setCategoryId(phones.getId());
        iphone.setCreatedAt(Instant.now());

        Product macbook = new Product();
        macbook.setName("MacBook");
        macbook.setCategoryId(laptops.getId());
        macbook.setCreatedAt(Instant.now());

        productRepo.saveAll(List.of(iphone, macbook));

        // --- Variants ---
        variantRepo.saveAll(List.of(

                // iPhone
                createVariant(iphone.getId(), "999.99"),
                createVariant(iphone.getId(), "1199.99"),

                // MacBook
                createVariant(macbook.getId(), "1499.99"),
                createVariant(macbook.getId(), "1999.99")

        ));
    }

    private ProductVariant createVariant(Long productId, String price) {
        ProductVariant v = new ProductVariant();
        v.setProductId(productId);
        v.setPrice(new BigDecimal(price));
        return v;
    }
}
