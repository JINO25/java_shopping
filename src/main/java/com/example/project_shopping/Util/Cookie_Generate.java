package com.example.project_shopping.Util;

import com.example.project_shopping.Constant.ApplicationConstants;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Cookie_Generate {
    @Value("${cookie.accessToken}")
    private String expireCookieForAccessToken;

    @Value("${cookie.refreshToken}")
    private String expireCookieForRefreshToken;

    public Cookie generateCookieAccessToken(String token){
        jakarta.servlet.http.Cookie cookie =new jakarta.servlet.http.Cookie(ApplicationConstants.accessCookie, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        Integer expirationTime = ExpirationUtils.parseCookieExpires(expireCookieForAccessToken);
        cookie.setMaxAge(expirationTime);

        return cookie;
    }

    public Cookie generateCookieRefreshToken(String token){
        jakarta.servlet.http.Cookie cookie =new jakarta.servlet.http.Cookie(ApplicationConstants.refreshCookie, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        Integer expirationTime = ExpirationUtils.parseCookieExpires(expireCookieForRefreshToken);
        cookie.setMaxAge(expirationTime);

        return cookie;
    }

    public Cookie generateExpiredCookie(){
        Cookie cookie = new Cookie(ApplicationConstants.accessCookie,null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (new Date().getTime()+3000));
//        cookie.setDomain("localhost");
        return cookie;
    }


}
