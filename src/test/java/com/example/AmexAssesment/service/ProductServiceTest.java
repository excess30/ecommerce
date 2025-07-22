package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.ProductEntity;
import com.example.AmexAssesment.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductsByCategory() {
        ProductEntity p1 = new ProductEntity();
        p1.setProductName("iPhone 14");
        p1.setCategory("Electronics");
        List<ProductEntity> products = Arrays.asList(p1);
        when(productRepository.findByCategory("Electronics")).thenReturn(products);
        List<ProductEntity> result = productService.getProductsByCategory("Electronics");
        assertEquals(1, result.size());
        assertEquals("iPhone 14", result.get(0).getProductName());
        verify(productRepository, times(1)).findByCategory("Electronics"); // Good practice to verify interactions
    }

    @Test
    void testGetProductByName() {
        ProductEntity p = new ProductEntity();
        p.setProductName("iPhone 14");
        when(productRepository.findByProductName("iPhone 14")).thenReturn(p);
        ProductEntity result = productService.getProductByName("iPhone 14");
        assertNotNull(result);
        assertEquals("iPhone 14", result.getProductName());
        verify(productRepository, times(1)).findByProductName("iPhone 14"); // Verify interaction
    }

    @Test
    void testConcurrentStockDecreaseWithLimitedAvailability() throws InterruptedException {
        Long productId = 1L;
        int initialStock = 5;
        int numConcurrentRequests = 10;
        int quantityToDecreasePerRequest = 1;
        AtomicInteger mockedStockQuantity = new AtomicInteger(initialStock);
        ProductEntity mockProduct = new ProductEntity();
        mockProduct.setId(productId);
        mockProduct.setProductName("Test Product");
        mockProduct.setCategory("Test Category");
        mockProduct.setQuantity(initialStock);
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(invocation -> {
            ProductEntity productToSave = invocation.getArgument(0);
            mockedStockQuantity.set(productToSave.getQuantity());
            return productToSave;
        });
        ExecutorService executor = Executors.newFixedThreadPool(numConcurrentRequests);
        CountDownLatch latch = new CountDownLatch(1); // To synchronize start of all threads
        AtomicInteger successfulDecreases = new AtomicInteger(0);
        AtomicInteger failedDecreases = new AtomicInteger(0);
        System.out.println("Starting concurrent stock decrease test for Product ID: " + productId + " with initial stock: " + initialStock);
        for (int i = 0; i < numConcurrentRequests; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    boolean success = productService.decreaseStock(productId, quantityToDecreasePerRequest);
                    if (success) {
                        successfulDecreases.incrementAndGet();
                    } else {
                        failedDecreases.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    fail("Thread interrupted during test execution: " + e.getMessage());
                } catch (Exception e) {
                    failedDecreases.incrementAndGet();
                    System.err.println("Exception during stock decrease: " + e.getMessage());
                }
            });
        }
        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Test finished. Successful decreases: " + successfulDecreases.get() + ", Failed decreases: " + failedDecreases.get());
        System.out.println("Final mocked stock quantity: " + mockedStockQuantity.get());
        assertEquals(initialStock, successfulDecreases.get(),
                "Number of successful stock decreases should match initial stock.");
        assertEquals(numConcurrentRequests - initialStock, failedDecreases.get(),
                "Number of failed stock decreases should be total requests minus initial stock.");
        assertEquals(0, mockedStockQuantity.get(), "Final stock quantity should be 0.");
        verify(productRepository, times(numConcurrentRequests)).findById(productId);
        verify(productRepository, times(initialStock)).save(any(ProductEntity.class));
    }
}