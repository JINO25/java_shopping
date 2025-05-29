package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Auth.LoginDTO;
import com.example.project_shopping.DTO.Auth.PassResetDTO;

public interface AuthService {
    boolean login(LoginDTO loginDTO);
    boolean forgotPassword(String email);
    boolean resetPassword(PassResetDTO passResetDTO);
}
