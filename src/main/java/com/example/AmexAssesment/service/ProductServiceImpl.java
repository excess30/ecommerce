package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.ProductEntity;
import com.example.AmexAssesment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public List<ProductEntity> getProductsByCategory(String category){
        return productRepository.findByCategory(category);
    }

    @Override
    public ProductEntity getProductByName(String product_name){
        return productRepository.findByProductName(product_name);
    }

    @Override
    public List<ProductEntity> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable).getContent();
    }

    @Override
    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    @Override
    public ProductEntity updateProduct(ProductEntity product) {
        if (product.getId() == null || !productRepository.existsById(product.getId())) {
            throw new RuntimeException("Product not found");
        }
        return productRepository.save(product);
    }

    @Override
    public ProductEntity markOutOfStock(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setCategory("OUT_OF_STOCK"); // Or set a boolean flag like product.setOutOfStock(true);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public synchronized boolean decreaseStock(Long productId, int quantity) {
        Optional<ProductEntity> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            System.out.println("Product not found with ID: " + productId);
            return false;
        }

        ProductEntity product = productOptional.get();
        if (product.getQuantity() < quantity) {
            System.out.println("Insufficient stock for product " + productId +
                    " (Available: " + product.getQuantity() + ", Requested: " + quantity + ")");
            return false;
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        System.out.println("Successfully decreased stock for product " + productId +
                " by " + quantity + ". New stock: " + product.getQuantity());
        return true;
    }

    @Transactional
    public ProductEntity setStock(Long productId, int quantity) {
        ProductEntity product = productRepository.findById(productId).orElse(new ProductEntity());
        product.setId(productId);
        product.setProductName("Test Product " + productId);
        product.setQuantity(quantity);
        return productRepository.save(product);
    }
}