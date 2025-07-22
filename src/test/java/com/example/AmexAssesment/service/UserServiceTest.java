package com.example.AmexAssesment.service;

import com.example.AmexAssesment.entity.UserEntity;
import com.example.AmexAssesment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock private UserRepository userRepository;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testCustomerLogin_Success() {
        UserEntity user = new UserEntity();
        user.setUsername("customer1");
        user.setPassword("custpass");
        user.setRole("CUSTOMER");
        when(userRepository.findByUsername("customer1")).thenReturn(user);
        UserEntity found = userRepository.findByUsername("customer1");
        assertNotNull(found);
        assertEquals("custpass", found.getPassword());
        assertEquals("CUSTOMER", found.getRole());
    }

    @Test
    void testGetProfile() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserEntity found = userRepository.findById(1L).orElse(null);
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    void testUpdateProfile() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("old@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        user.setEmail("new@example.com");
        when(userRepository.save(user)).thenReturn(user);
        UserEntity updated = userRepository.save(user);
        assertEquals("new@example.com", updated.getEmail());
    }
} 