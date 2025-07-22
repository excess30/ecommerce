package com.example.AmexAssesment.controller;

import com.example.AmexAssesment.entity.OrderEntity;
import com.example.AmexAssesment.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // Place an order (from cart)
    @PostMapping("/place")
    public OrderEntity placeOrder(@RequestParam Long userId, @RequestParam Long cartId) {
        return orderService.placeOrder(userId, cartId);
    }

    // View order history for a user
    @GetMapping("/history")
    public List<OrderEntity> getOrderHistory(@RequestParam Long userId) {
        return orderService.getOrderHistory(userId);
    }

    // View specific order
    @GetMapping("/view")
    public OrderEntity getOrder(@RequestParam Long orderId) {
        return orderService.getOrder(orderId);
    }
} 