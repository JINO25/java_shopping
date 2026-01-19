package com.example.project_shopping.DTO.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public record UserAuthDTO (
    Integer id,
    String name,
    String email,
    String avatar,
    String role
){}
