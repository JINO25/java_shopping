package com.example.project_shopping.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Auth {

    public static Integer getCurrentUserID(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            return (Integer) authentication.getDetails();
        }
        return null;
    }


}
