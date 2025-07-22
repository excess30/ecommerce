package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.OrderEntity;
import java.util.List;

public interface OrderService {
    OrderEntity placeOrder(Long userId, Long cartId);
    List<OrderEntity> getOrderHistory(Long userId);
    OrderEntity getOrder(Long orderId);
} 