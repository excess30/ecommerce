package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    List<ProductEntity> getProductsByCategory(String category);
    ProductEntity getProductByName(String product_name);
    List<ProductEntity> getAllProducts(int page, int size);
    ProductEntity createProduct(ProductEntity product);
    ProductEntity updateProduct(ProductEntity product);
    ProductEntity markOutOfStock(Long productId);
    boolean decreaseStock(Long productId, int quantity);
}
