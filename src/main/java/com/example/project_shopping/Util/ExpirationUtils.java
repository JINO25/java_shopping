package com.example.project_shopping.Util;

public class ExpirationUtils {
    public static Integer parseCookieExpires(String expires) {
        Integer timeInMillis = 0;
        if (expires.endsWith("d")) {
            int days = Integer.parseInt(expires.replace("d", ""));
            timeInMillis = days * 24 * 60 * 60 * 1000;
        } else if (expires.endsWith("h")) {
            int hours = Integer.parseInt(expires.replace("h", ""));
            timeInMillis = hours * 60 * 60 * 1000;
        } else if (expires.endsWith("m")) {
            int minutes = Integer.parseInt(expires.replace("m", ""));
            timeInMillis = minutes * 60 * 1000;
        }
        // Nếu không phải d, h, m thì mặc định là 0
        return timeInMillis;
    }

    public static Long parseJWTExpires(String expires) {
        Long timeInMillis = 0L;
        if (expires.endsWith("d")) {
            int days = Integer.parseInt(expires.replace("d", ""));
            timeInMillis = days * 24L * 60 * 60 * 1000;
        } else if (expires.endsWith("h")) {
            int hours = Integer.parseInt(expires.replace("h", ""));
            timeInMillis = hours * 60L * 60 * 1000;
        } else if (expires.endsWith("m")) {
            int minutes = Integer.parseInt(expires.replace("m", ""));
            timeInMillis = minutes * 60L * 1000;
        }
        // Nếu không phải d, h, m thì mặc định là 0
        return timeInMillis;
    }
}
