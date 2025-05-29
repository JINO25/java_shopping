package com.example.project_shopping.Service;


import com.example.project_shopping.DTO.Auth.PassResetDTO;

public interface EmailService {
    boolean sendToken(String email);
    boolean verifyToken(String email,String token);
    void sendEmail(String from, String to, String subject, String content);
}
