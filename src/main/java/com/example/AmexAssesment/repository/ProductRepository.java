package com.example.AmexAssesment.repository;

import com.example.AmexAssesment.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    List<ProductEntity> findByCategory(String category);
    ProductEntity findByProductName(String productName);
}
