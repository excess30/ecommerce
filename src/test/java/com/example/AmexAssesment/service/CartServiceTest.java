package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.CartEntity;
import com.example.AmexAssesment.entity.CartItemEntity;
import com.example.AmexAssesment.entity.ProductEntity;
import com.example.AmexAssesment.entity.UserEntity;
import com.example.AmexAssesment.repository.CartRepository;
import com.example.AmexAssesment.repository.ProductRepository;
import com.example.AmexAssesment.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart_NewCart() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCartId(10L);

        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setQuantity(5);

        CartEntity newCart = new CartEntity();
        newCart.setId(10L);
        newCart.setItems(new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findById(10L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(CartEntity.class))).thenReturn(newCart);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(CartEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        CartEntity cart = cartService.addToCart(1L, 1L, 2);
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
        assertEquals(product, cart.getItems().get(0).getProduct());
    }

    @Test
    void testAddToCart_ExistingCart_AddsQuantity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCartId(10L);

        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setQuantity(10);

        CartItemEntity item = new CartItemEntity();
        item.setProduct(product);
        item.setQuantity(3);

        CartEntity cart = new CartEntity();
        cart.setId(10L);
        ArrayList<CartItemEntity> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findById(10L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(CartEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        CartEntity updatedCart = cartService.addToCart(1L, 1L, 2);
        assertEquals(1, updatedCart.getItems().size());
        assertEquals(5, updatedCart.getItems().get(0).getQuantity());
    }

    @Test
    void testAddToCart_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(1L, 1L, 1));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void testAddToCart_ProductNotFound() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCartId(10L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findById(10L)).thenReturn(Optional.empty());
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> cartService.addToCart(1L, 1L, 1));
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    @Test
    void testAddToCart_InsufficientProductQuantity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCartId(10L);

        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setQuantity(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findById(10L)).thenReturn(Optional.empty());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception ex = assertThrows(RuntimeException.class, () -> cartService.addToCart(1L, 1L, 2));
        assertTrue(ex.getMessage().contains("Insufficient product quantity"));
    }

    @Test
    void testViewCart_Success() {
        CartEntity cart = new CartEntity();
        cart.setId(10L);
        when(cartRepository.findById(10L)).thenReturn(Optional.of(cart));
        CartEntity result = cartService.viewCart(10L);
        assertEquals(10L, result.getId());
    }

    @Test
    void testViewCart_NotFound() {
        when(cartRepository.findById(10L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> cartService.viewCart(10L));
        assertTrue(ex.getMessage().contains("Cart not found"));
    }
}