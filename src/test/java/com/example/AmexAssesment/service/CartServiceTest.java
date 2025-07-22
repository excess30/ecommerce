package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.CartEntity;
import com.example.AmexAssesment.entity.CartItemEntity;
import com.example.AmexAssesment.entity.ProductEntity;
import com.example.AmexAssesment.repository.CartRepository;
import com.example.AmexAssesment.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart_NewCart() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findById(null)).thenReturn(Optional.empty());
        when(cartRepository.save(any(CartEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        CartEntity cart = cartService.addToCart(null, 1L, 2);
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testViewCart() {
        CartEntity cart = new CartEntity();
        cart.setId(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        CartEntity result = cartService.viewCart(1L);
        assertEquals(1L, result.getId());
    }
} 