package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.CartEntity;
import com.example.AmexAssesment.entity.CartItemEntity;
import com.example.AmexAssesment.entity.ProductEntity;
import com.example.AmexAssesment.repository.CartRepository;
import com.example.AmexAssesment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    public CartEntity addToCart(Long cartId, Long productId, int quantity) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    return cartRepository.save(newCart);
                });
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItemEntity newItem = new CartItemEntity();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }
        return cartRepository.save(cart);
    }

    public CartEntity viewCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
} 