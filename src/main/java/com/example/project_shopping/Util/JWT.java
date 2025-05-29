package com.example.project_shopping.Util;

import com.example.project_shopping.Constant.ApplicationConstants;
import com.example.project_shopping.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWT {
    @Value("${jwt.secret.key}")
    private String jwtSecret;

    @Value("${jwt.default.value}")
    private String jwtDefault;

    @Value("${jwt.expires}")
    private String jwtExpires;

    @Value("${jwt.refreshToken}")
    private String jwtRefreshExpires;
    public String createJWT(Authentication authentication, Environment env){
        String jwt="";
        Long expirationTime = ExpirationUtils.parseJWTExpires(jwtExpires);
        String secret = env.getProperty(jwtSecret,jwtDefault);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//        System.out.println("JWT Name: "+authentication.getName());
//        System.out.println("JWT Principal: "+authentication.getPrincipal());
//        System.out.println("JWT detail: "+authentication.getDetails());
        jwt = Jwts.builder().issuer("ShoppingApp").subject(authentication.getName())
                .claim("userID", authentication.getDetails())
                .claim("authorities", authentication.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis()+expirationTime))
                .signWith(secretKey).compact();

        return jwt;
    }

    public String createRefreshToken(Authentication authentication, Environment env) {
        String jwt="";
        Long expirationTime = ExpirationUtils.parseJWTExpires(jwtExpires);
        String secret = env.getProperty(jwtSecret,jwtDefault);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        jwt = Jwts.builder().issuer("ShoppingApp").subject(authentication.getName())
                .claim("userID", authentication.getDetails())
                .claim("authorities", authentication.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis()+expirationTime))
                .signWith(secretKey).compact();

        return jwt;
    }

    public Map<String,String> getUsernameFromToken(String token, Environment env) {
        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        if (secretKey != null) {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Integer userId = (Integer) claims.get("userID");
            String email = claims.getSubject();
            String authorities = String.valueOf(claims.get("authorities"));
            Map<String, String> infor = new HashMap<>();
            infor.put("userId", String.valueOf(userId));
            infor.put("email",email);
            infor.put("authorities",authorities);
            return infor;
        }
        return null;
    }


}
