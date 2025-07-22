package com.example.AmexAssesment.controller;

import com.example.AmexAssesment.entity.UserEntity;
import com.example.AmexAssesment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // Customer login
    @PostMapping("/login")
    public UserEntity customerLogin(@RequestParam String username, @RequestParam String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password) && "CUSTOMER".equals(user.getRole())) {
            return user;
        }
        throw new RuntimeException("Invalid credentials or not a customer");
    }

    // View profile
    @GetMapping("/profile")
    public UserEntity getProfile(@RequestParam Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Update profile
    @PutMapping("/profile")
    public UserEntity updateProfile(@RequestParam Long userId, @RequestBody UserEntity updatedUser) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(updatedUser.getEmail());
        user.setAddress(updatedUser.getAddress());
        user.setPhone(updatedUser.getPhone());
        // Optionally update other fields
        return userRepository.save(user);
    }
} 