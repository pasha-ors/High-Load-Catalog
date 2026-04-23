package com.data_store.catalog.repository;

import com.data_store.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {}
