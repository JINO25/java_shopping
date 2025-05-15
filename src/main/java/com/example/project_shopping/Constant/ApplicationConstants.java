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

    public static String JWT_SECRET_KEY;
    public static String JWT_SECRET_DEFAULT_VALUE;

    @Override
    public void afterPropertiesSet() {
        JWT_SECRET_KEY = jwtSecretKey;
        JWT_SECRET_DEFAULT_VALUE = jwtDefaultValue;
    }
}

