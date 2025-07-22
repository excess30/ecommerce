package com.example.AmexAssesment.repository;

import com.example.AmexAssesment.entity.OrderEntity;
import com.example.AmexAssesment.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByUser(UserEntity user);
} 