package com.example.project_shopping.Util;

import com.example.project_shopping.Entity.User;
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
import java.util.stream.Collectors;

@Component
public class JWT {
    @Value("${jwt.secret.key}")
    private String jwtSecret;

    @Value("${jwt.default.value}")
    private String jwtDefault;

    @Value("${jwt.expires}")
    private String jwtExpires;
    public String createJWT(Authentication authentication, Environment env){
        String jwt="";
        Long expirationTime = ExpirationUtils.parseJWTExpires(jwtExpires);
        String secret = env.getProperty(jwtSecret,jwtDefault);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        jwt = Jwts.builder().issuer("ShoppingApp").subject(authentication.getName())
                .claim("userID", authentication.getPrincipal())
                .claim("authorities", authentication.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis()+expirationTime))
                .signWith(secretKey).compact();

        return jwt;
    }
}
