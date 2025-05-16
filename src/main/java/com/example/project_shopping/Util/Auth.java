package com.example.project_shopping.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Auth {

    public static Integer getCurrentUserID(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
//            System.out.println(authentication.getAuthorities());
            return (Integer) authentication.getPrincipal();
        }
        return null;
    }


}
