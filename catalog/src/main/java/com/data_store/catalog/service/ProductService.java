package com.data_store.catalog.service;

import com.data_store.catalog.dto.CreateProductRequest;
import com.data_store.catalog.dto.ProductPreviewDTO;
import com.data_store.catalog.dto.UpdateProductRequest;
import com.data_store.catalog.dto.UpdateVariantRequest;
import com.data_store.catalog.model.Product;
import com.data_store.catalog.model.ProductVariant;
import com.data_store.catalog.repository.CategoryRepository;
import com.data_store.catalog.repository.ProductRepository;
import com.data_store.catalog.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    @Cacheable(
            value = "products",
            condition = "#lastId == null",
            key = "(#categoryId == null ? 'all' : #categoryId) + '_' + " +
                    "(#search == null || #search.isBlank() ? 'none' : #search.trim().toLowerCase())"
    )
    public List<ProductPreviewDTO> getProducts(
            Long categoryId,
            String search,
            Long lastId)
    {
        String normalizedSearch = (search == null || search.isBlank()) ? null : search.trim().toLowerCase();

        return productRepository.findProductPreview(
                categoryId,
                normalizedSearch,
                lastId,
                PageRequest.of(0, 20)
        ).stream()
                .map(p -> new ProductPreviewDTO(
                        p.id(),
                        p.name(),
                        categoryService.getName(p.categoryId()),
                        p.price()
                )).toList();
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public Long createProduct(CreateProductRequest request){

        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Name required");
        }

        if (request.prices() == null || request.prices().isEmpty()) {
            throw new IllegalArgumentException("Prices required");
        }

        if (request.prices().stream().anyMatch(p -> p == null || p.signum() <= 0)) {
            throw new IllegalArgumentException("Invalid price");
        }

        if (!categoryRepository.existsById(request.categoryId())) {
            throw new IllegalArgumentException("Category not found");
        }

        Product product = new Product();
        product.setName(request.name().trim());
        product.setCategoryId(request.categoryId());
        product.setCreatedAt(Instant.now());

        productRepository.save(product);

        List<ProductVariant> variants = request.prices().stream()
                .map(
                        price -> {
                            ProductVariant productVariant = new ProductVariant();
                            productVariant.setProductId(product.getId());
                            productVariant.setPrice(price);
                            return productVariant;
                        }
                ).toList();

        variantRepository.saveAll(variants);

        return product.getId();
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void updateProduct(Long id, UpdateProductRequest request){

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        if(request.name() != null && !request.name().isBlank()){
            product.setName(request.name().trim());
        }

        if(request.categoryId() != null){
            if (!categoryRepository.existsById(request.categoryId())) {
                throw new IllegalArgumentException("Category not found");
            }
            product.setCategoryId(request.categoryId());
        }
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void updateVariant(Long variantId, UpdateVariantRequest req) {

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        if (req.price() == null || req.price().signum() <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        variant.setPrice(req.price());
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        variantRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void deleteVariant(Long variantId) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        if (variantRepository.countByProductId(variant.getProductId()) <= 1) {
            throw new IllegalStateException("Cannot delete last variant");
        }

        variantRepository.deleteById(variantId);
    }
}
