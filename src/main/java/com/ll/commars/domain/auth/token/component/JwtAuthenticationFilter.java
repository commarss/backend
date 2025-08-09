package com.ll.commars.domain.auth.token.component;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ll.commars.domain.auth.token.JwtAuthenticationToken;
import com.ll.commars.domain.auth.token.TokenProvider;
import com.ll.commars.domain.auth.token.entity.JwtClaims;
import com.ll.commars.domain.auth.token.entity.TokenValue;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;
	private final UserDetailsService userDetailsService;

	@Override
	public void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String tokenValue = authHeader.substring("Bearer ".length());

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			TokenValue jwtTokenValue = TokenValue.of(tokenValue);
			JwtClaims jwtClaims = tokenProvider.parseClaim(jwtTokenValue);

			UserDetails userDetails = userDetailsService.loadUserByUsername(
				String.valueOf(jwtClaims.privateClaims().userId())
			);

			JwtAuthenticationToken authentication = JwtAuthenticationToken.authenticated(
				userDetails,
				userDetails.getAuthorities()
			);

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
