package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Auth.LoginDTO;

public interface AuthService {
    boolean login(LoginDTO loginDTO);
}
