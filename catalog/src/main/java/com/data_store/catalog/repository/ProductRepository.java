package com.data_store.catalog.repository;

import com.data_store.catalog.dto.ProductPreviewDB;
import com.data_store.catalog.model.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT new com.data_store.catalog.dto.ProductPreviewDB(
                p.id,
                p.name,
                p.categoryId,
                (
                    SELECT MIN(v.price)
                    FROM ProductVariant v
                    WHERE v.productId = p.id
                )
            )
            FROM Product p
            WHERE (:categoryId IS NULL OR p.categoryId = :categoryId)
            AND( :search IS NULL OR p.name ILIKE CONCAT('%', CAST(:search AS string), '%'))
            AND( :lastId IS NULL OR p.id < :lastId)
            ORDER BY p.id DESC
    """)
    List<ProductPreviewDB> findProductPreview(
            @Param("categoryId") Long categoryId,
            @Param("search") String search,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

}
