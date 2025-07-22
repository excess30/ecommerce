package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.*;
import com.example.AmexAssesment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderEntity placeOrder(Long userId, Long cartId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        if (cart.getItems().isEmpty()) throw new RuntimeException("Cart is empty");
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStatus("PLACED");
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (CartItemEntity cartItem : cart.getItems()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        order = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        cart.getItems().clear();
        cartRepository.save(cart);
        return order;
    }

    @Override
    public List<OrderEntity> getOrderHistory(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findAllByUser(user);
    }

    @Override
    public OrderEntity getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }
} 