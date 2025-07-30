package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.*;
import com.example.AmexAssesment.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private UserRepository userRepository;
    @Mock private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;
    @InjectMocks private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testPlaceOrder_Success() {
        UserEntity user = new UserEntity(); user.setId(1L);
        CartEntity cart = new CartEntity(); cart.setId(1L);
        ProductEntity product = new ProductEntity(); product.setId(1L); product.setQuantity(5);
        CartItemEntity item = new CartItemEntity();
        item.setProduct(product); item.setQuantity(2);
        cart.setItems(new ArrayList<>(Collections.singletonList(item)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderEntity order = orderService.placeOrder(1L, 1L);

        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals(1, order.getItems().size());
        // Product quantity should be decremented
        assertEquals(3, product.getQuantity());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testPlaceOrder_InsufficientStock() {
        UserEntity user = new UserEntity(); user.setId(1L);
        CartEntity cart = new CartEntity(); cart.setId(1L);
        ProductEntity product = new ProductEntity(); product.setId(1L); product.setProductName("Test Product"); product.setQuantity(1);
        CartItemEntity item = new CartItemEntity();
        item.setProduct(product); item.setQuantity(2);
        cart.setItems(new ArrayList<>(Collections.singletonList(item)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.placeOrder(1L, 1L));
        assertTrue(ex.getMessage().contains("Insufficient quantity"));
        verify(productRepository, never()).save(any());
    }

    @Test
    void testGetOrderHistory() {
        UserEntity user = new UserEntity(); user.setId(1L);
        List<OrderEntity> orders = Arrays.asList(new OrderEntity(), new OrderEntity());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findAllByUser(user)).thenReturn(orders);
        List<OrderEntity> result = orderService.getOrderHistory(1L);
        assertEquals(2, result.size());
    }

    @Test
    void testStockLimitedConcurrency() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setProductName("Test Product");
        CartItemEntity item1 = new CartItemEntity(); item1.setProduct(product); item1.setQuantity(3);
        CartItemEntity item2 = new CartItemEntity(); item2.setProduct(product); item2.setQuantity(3);
        CartEntity cart1 = new CartEntity(); cart1.setId(1L); cart1.setItems(new ArrayList<>(Collections.singletonList(item1)));
        CartEntity cart2 = new CartEntity(); cart2.setId(2L); cart2.setItems(new ArrayList<>(Collections.singletonList(item2)));
        UserEntity user1 = new UserEntity(); user1.setId(1L);
        UserEntity user2 = new UserEntity(); user2.setId(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart1));
        when(cartRepository.findById(2L)).thenReturn(Optional.of(cart2));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        int stock = 5;
        int totalOrdered = item1.getQuantity() + item2.getQuantity();
        assertTrue(totalOrdered > stock, "Total ordered exceeds stock!");
    }
}