package com.example.project_shopping.DTO.Auth;

public record RegisterRequest(
        String name,
        String email,
        String password
) {}

