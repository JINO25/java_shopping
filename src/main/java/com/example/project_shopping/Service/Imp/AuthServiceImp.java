package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Auth.LoginDTO;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Repository.UserRepository;
import com.example.project_shopping.Service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImp implements AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
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
}
