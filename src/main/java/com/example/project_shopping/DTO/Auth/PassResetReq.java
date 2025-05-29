package com.example.project_shopping.DTO.Auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassResetReq {
    @NotEmpty(message = "Email không được để trống")
    private String email;
}
