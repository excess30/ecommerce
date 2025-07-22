package com.example.AmexAssesment.controller;

import com.example.AmexAssesment.entity.CartEntity;
import com.example.AmexAssesment.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public CartEntity addToCart(@RequestParam(required = false) Long cartId,
                                @RequestParam Long productId,
                                @RequestParam int quantity) {
        return cartService.addToCart(cartId, productId, quantity);
    }

    @GetMapping("/view")
    public CartEntity viewCart(@RequestParam Long cartId) {
        return cartService.viewCart(cartId);
    }
} 