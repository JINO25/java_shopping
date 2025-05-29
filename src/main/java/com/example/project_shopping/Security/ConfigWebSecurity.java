package com.example.project_shopping.Security;

import com.example.project_shopping.Exception.CustomAccessDeniedHandler;
import com.example.project_shopping.Filter.JWTValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@AllArgsConstructor
public class ConfigWebSecurity {
    private final AuthenticationProvider authenticationProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authenticationProvider(authenticationProvider)
                .sessionManagement(sessonCofig->sessonCofig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors->cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("http://localhost:8080","http://127.0.0.1:5500"));
                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .addFilterBefore(new JWTValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((author)->{
                    author.requestMatchers("/user/create","/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET,"/product/**").permitAll()
                            .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                            .requestMatchers("/order/payment-success").permitAll()
                            .requestMatchers("/order/payment-success-list").permitAll()


                            //user
                            .requestMatchers(HttpMethod.GET,"/user").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"/user/**").hasAnyRole("ADMIN","USER")
                            .requestMatchers("/user/create/admin","/user/create/seller").hasRole("ADMIN")
                            //product
                            .requestMatchers("/product/create").hasRole("SELLER")
                            .requestMatchers("/product/update/**").hasRole("SELLER")
                            .requestMatchers("/product/delete/**").hasAnyRole("SELLER","ADMIN")
                            //order
                            .requestMatchers(HttpMethod.GET,"/order/my").hasRole("USER")
                            .requestMatchers(HttpMethod.GET,"/order/**").hasAnyRole("SELLER","ADMIN")
                            .requestMatchers("/order/*/status").hasRole("SELLER")
                            .requestMatchers("/order/delete/**").hasRole("SELLER")
                            .requestMatchers("/order/*/cancel").hasRole("USER")
                            //cart
                            .requestMatchers("/cart/**").hasRole("USER")
                            //bill
                            .requestMatchers("/bills/user").hasRole("USER")
                            .requestMatchers("/bills/seller", "/bills/update").hasRole("SELLER")
                            .requestMatchers("/bills/**").hasRole("ADMIN")
                            //image
                            .requestMatchers("/images/**").hasAnyRole("ADMIN","SELLER","USER")
                            //general
                            .requestMatchers("/user/**","/addresses", "/order/**").authenticated()
                            .anyRequest().authenticated();
                })
                .exceptionHandling((ex)->{
                    ex.authenticationEntryPoint(customAuthenticationEntryPoint);
                    ex.accessDeniedHandler(customAccessDeniedHandler);
                })
                .csrf().disable();
        return httpSecurity.build();
    }
}
