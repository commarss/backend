package com.ll.commars.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtAuthenticationFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // JWT 토큰을 검증하는 로직
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String accessToken = request.getHeader("Authorization");

        if (accessToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    }
    // JWT 토큰을 검증하는 필터

}
