package com.example.project_shopping.DTO.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PassResetDTO {
    @NotEmpty(message = "Email not empty!")
    @Email
    private String email;
    private String token;
    @NotEmpty(message = "Password not empty!")
    private String newPass;
}
