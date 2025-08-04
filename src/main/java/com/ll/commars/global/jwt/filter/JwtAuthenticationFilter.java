package com.ll.commars.global.jwt.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import com.ll.commars.global.jwt.component.JwtProvider;
import com.ll.commars.global.jwt.entity.JwtAuthenticationToken;
import com.ll.commars.global.jwt.entity.JwtToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final UserService userService;

	@Value("${jwt.excluded-urls}")
	private String excludeUrls;

	@Value(("${jwt.excluded-base-urls}"))
	private String excludeBaseUrls;

	public JwtAuthenticationFilter(JwtProvider jwtProvider, UserService userService) {
		this.jwtProvider = jwtProvider;
		this.userService = userService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();

		String[] excludeUrlPatterns = excludeUrls.split(",");
		String[] excludeBaseUrlPatterns = excludeBaseUrls.split(",");

		for (String excludeBaseUrlPattern : excludeBaseUrlPatterns) {
			excludeBaseUrlPattern = excludeBaseUrlPattern.trim();
			if (requestURI.startsWith(excludeBaseUrlPattern)) {
				System.out.println("🔒 토큰 검증 필터 제외 URL :" + requestURI);
				filterChain.doFilter(request, response);
				return;
			}
		}

		for (String excludeUrlPattern : excludeUrlPatterns) {
			excludeUrlPattern = excludeUrlPattern.trim();
			if (requestURI.matches(excludeUrlPattern)) {
				System.out.println("🔒 토큰 검증 필터 제외 URL :" + requestURI);
				return;
			}
		}

		System.out.println("🔒 토큰 검증 필터 실행 URL :" + requestURI);

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String accessToken = authHeader.substring("Bearer ".length());
			try {
				if (jwtProvider.validateToken(accessToken)) {
					JwtToken jwtToken = jwtProvider.extractJwtToken(accessToken);
					Optional<User> user = userService.findByIdAndEmail(jwtToken.getId(), jwtToken.getEmail());

					if (user.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
						UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(
							String.valueOf(user.get().getId())).password("").authorities("USER").build();
						JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
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
