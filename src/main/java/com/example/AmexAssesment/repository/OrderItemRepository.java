package com.example.AmexAssesment.repository;

import com.example.AmexAssesment.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
} 