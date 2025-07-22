package com.example.AmexAssesment.repository;

import com.example.AmexAssesment.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
} 