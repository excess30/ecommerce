package com.example.AmexAssesment.controller;

import com.example.AmexAssesment.entity.ProductEntity;
import com.example.AmexAssesment.entity.UserEntity;
import com.example.AmexAssesment.repository.UserRepository;
import com.example.AmexAssesment.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;

    // Simple admin login (returns user if credentials match and role is ADMIN)
    @PostMapping("/login")
    public UserEntity login(@RequestParam String username, @RequestParam String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password) && "ADMIN".equals(user.getRole())) {
            return user;
        }
        throw new RuntimeException("Invalid credentials or not an admin");
    }

    // View all products (with optional pagination)
    @GetMapping("/products")
    public List<ProductEntity> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(page, size);
    }

    // View products by category
    @GetMapping("/products/category")
    public List<ProductEntity> getProductsByCategory(@RequestParam String category) {
        return productService.getProductsByCategory(category);
    }

    // View specific product by name
    @GetMapping("/products/view")
    public ProductEntity getProductByName(@RequestParam String product_name) {
        return productService.getProductByName(product_name);
    }

    // Create new product
    @PostMapping("/products/create")
    public ProductEntity createProduct(@RequestBody ProductEntity product) {
        return productService.createProduct(product);
    }

    // Update product
    @PutMapping("/products/update")
    public ProductEntity updateProduct(@RequestBody ProductEntity product) {
        return productService.updateProduct(product);
    }

    // Mark product out of stock
    @PutMapping("/products/outofstock")
    public ProductEntity markOutOfStock(@RequestParam Long productId) {
        return productService.markOutOfStock(productId);
    }
} 