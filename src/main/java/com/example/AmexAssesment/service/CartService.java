package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.CartEntity;
import com.example.AmexAssesment.entity.CartItemEntity;
import com.example.AmexAssesment.entity.ProductEntity;
import com.example.AmexAssesment.entity.UserEntity;
import com.example.AmexAssesment.repository.CartRepository;
import com.example.AmexAssesment.repository.ProductRepository;
import com.example.AmexAssesment.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public CartEntity addToCart(Long userId, Long productId, int quantity) {
        UserEntity user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found!"));
        Long cartId = user.getCartId();
        CartEntity cart = cartRepository.findById(cartId)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    return cartRepository.save(newCart);
                });
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient product quantity");
        }
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