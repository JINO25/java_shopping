package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Auth.LoginDTO;
import com.example.project_shopping.DTO.Auth.PassResetDTO;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Repository.UserRepository;
import com.example.project_shopping.Service.AuthService;
import com.example.project_shopping.Service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImp implements AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    @Override
    public boolean login(LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findUserByEmail(loginDTO.getEmail());
        if(userOptional.isEmpty()){
            return false;
        }

        User user = userOptional.get();

        if(!passwordEncoder.matches(loginDTO.getPassword(),user.getPassword())){
            return false;
        }

        return true;
    }

    @Override
    public boolean forgotPassword(String email) {
        boolean check = emailService.sendToken(email);
        return check;
    }

    @Override
    public boolean resetPassword(PassResetDTO passResetDTO) {
        boolean valid  = emailService.verifyToken(passResetDTO.getEmail(), passResetDTO.getToken());
        if(valid  == true){
            User user = userRepository.findUserByEmail(passResetDTO.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            String encodedPassword = passwordEncoder.encode(passResetDTO.getNewPass());
            user.setPassword(encodedPassword);

            userRepository.save(user);
            return true;
        }
        return false;
    }
}
