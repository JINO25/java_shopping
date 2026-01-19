package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Auth.LoginDTO;
import com.example.project_shopping.DTO.Auth.PassResetDTO;
import com.example.project_shopping.DTO.Auth.RegisterRequest;
import com.example.project_shopping.DTO.Auth.UserAuthDTO;
import com.example.project_shopping.Entity.Role;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Repository.RoleRepository;
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
    RoleRepository roleRepository;
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

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("EMAIL_EXISTS");
        }

        Role role = roleRepository.findByRole("ROLE_USER");
        if (role == null) {
            throw new RuntimeException("ROLE_NOT_FOUND");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);

        userRepository.save(user);
    }

}
