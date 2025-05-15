package com.example.project_shopping.Util;

import com.example.project_shopping.Entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Auth {
    public static Integer getCurrentUserID(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof User){
            return ((User) authentication.getPrincipal()).getId();
        }
        return null;
    }

    public static User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof User){
            return ((User) authentication.getPrincipal());
        }
        return null;
    }
}
