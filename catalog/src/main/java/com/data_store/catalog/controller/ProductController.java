package com.data_store.catalog.controller;

import com.data_store.catalog.dto.CreateProductRequest;
import com.data_store.catalog.dto.ProductPreviewDTO;
import com.data_store.catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductPreviewDTO> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long lastId
    ){
        return productService.getProducts(categoryId, search, lastId);
    }

    @PostMapping
    public Long createProduct(@RequestBody CreateProductRequest request){
        return productService.createProduct(request);
    }

    @PutMapping("/{id}")
    public void updateProduct(
            @PathVariable Long id,
            @RequestBody com.data_store.catalog.dto.UpdateProductRequest request
    ){
        productService.updateProduct(id, request);
    }

    @PutMapping("/variants/{variantId}")
    public void updateVariant(
            @PathVariable Long variantId,
            @RequestBody com.data_store.catalog.dto.UpdateVariantRequest request
    ){
        productService.updateVariant(variantId, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }

    @DeleteMapping("/variants/{variantId}")
    public void deleteVariant(@PathVariable Long variantId){
        productService.deleteVariant(variantId);
    }
}
