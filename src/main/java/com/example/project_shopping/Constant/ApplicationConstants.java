package com.example.project_shopping.Constant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConstants implements InitializingBean {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.default.value}")
    private String jwtDefaultValue;


    @Value("${spring.mail.email}")
    private String emailService;

    @Value("${spring.host}")
    private String hostWebsite;

    @Value("${spring.accessToken}")
    public String accessCookieTokenName;
    @Value("${spring.refreshToken}")
    public String refreshCookieTokenName;

    public static String JWT_SECRET_KEY;
    public static String JWT_SECRET_DEFAULT_VALUE;
    public static String email;
    public static String host;
    public static String accessCookie;
    public static String refreshCookie;
    @Override
    public void afterPropertiesSet() {
        JWT_SECRET_KEY = jwtSecretKey;
        JWT_SECRET_DEFAULT_VALUE = jwtDefaultValue;
        email=emailService;
        host=hostWebsite;
        accessCookie=accessCookieTokenName;
        refreshCookie=refreshCookieTokenName;
    }
}

