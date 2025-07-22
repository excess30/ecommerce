package com.example.AmexAssesment.controller;

import com.example.AmexAssesment.entity.ProductEntity;
import com.example.AmexAssesment.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerFacingController {
    @Autowired
    ProductService productService;

    //view products by category
    @GetMapping("/categoryProducts")
    public List<ProductEntity> getCategoryProducts(@RequestParam String category){
        return productService.getProductsByCategory(category);
    }

    //view specific product
    @GetMapping("/viewProduct")
    public ProductEntity getProducts(@RequestParam String product_name){
        return productService.getProductByName(product_name);
    }


}
