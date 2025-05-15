package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Auth.LoginDTO;
import com.example.project_shopping.Service.AuthService;
import com.example.project_shopping.Util.Cookie_Generate;
import com.example.project_shopping.Util.JWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;
    private AuthenticationManager authenticationManager;
    private JWT jwt;
    private Environment env;
    private Cookie_Generate cookieGenerate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e->{
                errorMsg.append(e.getDefaultMessage()).append("\n");
            });
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }

        boolean check = authService.login(loginDTO);

        if (check == false) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwt.createJWT(authentication,env);
        Cookie cookie = cookieGenerate.generateCookie(token);
        response.addCookie(cookie);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
