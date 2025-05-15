package com.example.project_shopping.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Cookie_Generate {
    @Value("${cookie.expires}")
    private String expireCookie;

    public jakarta.servlet.http.Cookie generateCookie(String token){
        jakarta.servlet.http.Cookie cookie =new jakarta.servlet.http.Cookie("cookieJWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        Integer expirationTime = ExpirationUtils.parseCookieExpires(expireCookie);
        cookie.setMaxAge(expirationTime);

        return cookie;
    }


}
