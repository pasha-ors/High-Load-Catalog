package com.data_store.catalog.service;

import com.data_store.catalog.model.Category;
import com.data_store.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(value = "category", key = "#id")
    public String getName(Long id){
        return categoryRepository.findById(id)
                .map(Category::getName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
