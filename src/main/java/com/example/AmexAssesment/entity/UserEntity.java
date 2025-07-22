package com.example.AmexAssesment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // CUSTOMER or ADMIN

    @Column
    private String email;

    @Column
    private String address;

    @Column
    private String phone;
} 