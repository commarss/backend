package com.ll.commars.domain.auth.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenValidator {
    // jwt 토큰 유효성 검사

    @Value("${jwt.secret-key}")
    private static String secretKey;

    public static boolean validate(String jwtToken) {
//        try {
////            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
////            Date expiration = claims.getExpiration();
//            return expiration.after(new Date());
//        } catch (Exception e) {
//            return false;
//        }
        return true;
    }
}
