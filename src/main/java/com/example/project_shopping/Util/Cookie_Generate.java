package com.example.project_shopping.Util;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Cookie_Generate {
    @Value("${cookie.expires}")
    private String expireCookie;

    public Cookie generateCookie(String token){
        jakarta.servlet.http.Cookie cookie =new jakarta.servlet.http.Cookie("cookieJWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        Integer expirationTime = ExpirationUtils.parseCookieExpires(expireCookie);
        cookie.setMaxAge(expirationTime);

        return cookie;
    }

    public Cookie generateExpiredCookie(){
        Cookie cookie = new Cookie("cookieJWT",null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (new Date().getTime()+3000));
        return cookie;
    }


}
