package com.example.project_shopping.Security;

import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@AllArgsConstructor
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        User user = userRepository.findByEmail(email);
        if (user != null){
            if (passwordEncoder.matches(pwd,user.getPassword())){
                return new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRole()))
                );
            }else{
                throw new BadCredentialsException("Email or password is incorrect!");
            }
        }else {
            throw new BadCredentialsException("User not found with email!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
