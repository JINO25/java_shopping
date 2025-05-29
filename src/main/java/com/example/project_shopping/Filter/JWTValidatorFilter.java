package com.example.project_shopping.Filter;

import com.example.project_shopping.Constant.ApplicationConstants;
import com.example.project_shopping.Exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

public class JWTValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (ApplicationConstants.accessCookie.equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        if (jwt != null) {
            try {
                Environment env = getEnvironment();
                if (env != null) {
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    if (secretKey != null) {
                        Claims claims = Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(jwt)
                                .getPayload();

                        Integer userId = (Integer) claims.get("userID");
                        String email = claims.getSubject();
                        String authorities = String.valueOf(claims.get("authorities"));
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                email, null,
//                                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                                Collections.singleton(new SimpleGrantedAuthority(authorities))
                        );
                        authentication.setDetails(userId);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        System.out.println("JWTVa Name: "+authentication.getName());
//                        System.out.println("JWTVa Principal: "+authentication.getPrincipal());
//                        System.out.println("JWTVa details: "+authentication.getDetails());
                    }
                }
            } catch (Exception exception) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String body = """
                    {
                      "timestamp": "%s",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "You are not logged in, please login again!"
                    }
                    """.formatted(java.time.LocalDateTime.now());

                response.getWriter().write(body);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] allowURL = {"/auth/login", "/product", "/user/create"};
        String reqURL = request.getRequestURI();

        return Arrays.asList(allowURL).contains(reqURL);
    }
}
