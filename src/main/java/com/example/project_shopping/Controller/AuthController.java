package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Auth.LoginDTO;
import com.example.project_shopping.DTO.Auth.PassResetDTO;
import com.example.project_shopping.DTO.Auth.PassResetReq;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

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

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken  = jwt.createJWT(authentication,env);
        String refreshToken = jwt.createRefreshToken(authentication, env);

        Cookie accessCookie  = cookieGenerate.generateCookieAccessToken(accessToken );
        response.addCookie(accessCookie);

        Cookie refreshCookie  = cookieGenerate.generateCookieRefreshToken(refreshToken);
        response.addCookie(refreshCookie);

        return new ResponseEntity<>(accessToken , HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        try {
            // Xác thực và lấy username từ refresh token
            Map<String, String> infor =jwt.getUsernameFromToken(refreshToken, env);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    infor.get("email") , null, Collections.singleton(new SimpleGrantedAuthority(infor.get("authorities"))));
            authentication.setDetails(Integer.parseInt(infor.get("userId")));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String newAccessToken = jwt.createJWT(authentication, env);

            Cookie accessCookie = cookieGenerate.generateCookieAccessToken(newAccessToken);
            response.addCookie(accessCookie);

            return new ResponseEntity<>("Refresh thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Refresh token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        Cookie cookie = cookieGenerate.generateExpiredCookie();
        response.addCookie(cookie);
        return new ResponseEntity<>(cookie,HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PassResetReq passResetReq, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e -> {
                errorMsg.append(e.getDefaultMessage()).append("\n");
            });
            return new ResponseEntity<>(errorMsg.toString(), HttpStatus.BAD_REQUEST);
        }

        boolean sent = authService.forgotPassword(passResetReq.getEmail());
        if(sent){
            return ResponseEntity.ok("Gửi email đặt lại mật khẩu thành công. Vui lòng kiểm tra hộp thư.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email đặt lại mật khẩu thất bại, vui lòng thử lại sau.");
        }
    }


    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PassResetDTO passResetDTO,
                                           @PathVariable String token,
                                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e -> {
                errorMsg.append(e.getDefaultMessage()).append("\n");
            });
            return new ResponseEntity<>(errorMsg.toString(), HttpStatus.BAD_REQUEST);
        }

        passResetDTO.setToken(token);

        boolean resetSuccess = authService.resetPassword(passResetDTO);

        if(resetSuccess){
            return ResponseEntity.ok("Đặt lại mật khẩu thành công.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token không hợp lệ hoặc đã hết hạn.");
        }
    }

}
