package com.example.AmexAssesment.repository;

import com.example.AmexAssesment.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
} 