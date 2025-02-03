package com.ll.commars.global.filter;

import com.ll.commars.global.jwt.component.JwtProvider;
import com.ll.commars.global.jwt.entity.JwtAuthenticationToken;
import com.ll.commars.global.jwt.entity.JwtToken;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        if (requestURI.startsWith("/api/public/") || requestURI.startsWith("/api/auth/google") || requestURI.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("authHeader = " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring("Bearer ".length());
            System.out.println("accessToken = " + accessToken);
            try {
                if (jwtProvider.validateToken(accessToken)) {
                    JwtToken jwtToken = jwtProvider.getJwtToken(accessToken);
                    String email = jwtToken.getEmail();
                    System.out.println("✅ 유효한 JWT 토큰: 사용자 - " + email);

                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(email).password("").authorities("USER").build();
                        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        jwtAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
                    }
                } else {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token");
                    return;
                }
            } catch (Exception e) {
                System.out.println("❌ JWT 토큰 검증 실패: " + e.getMessage());
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
