package com.ll.commars.domain.auth.token;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ll.commars.domain.auth.token.entity.JwtClaims;
import com.ll.commars.domain.auth.token.entity.JwtTokenValue;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

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
				JwtTokenValue jwtTokenValue = JwtTokenValue.of(tokenValue);
				JwtClaims jwtClaims = jwtProvider.parseClaim(jwtTokenValue);

				String email = jwtClaims.publicClaims().subject();
				Long userId = jwtClaims.privateClaims().userId();

				UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
					.username(String.valueOf(userId))
					.password("")
					.authorities("ROLE_USER")
					.build();

				JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails,
					userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		filterChain.doFilter(request, response);
	}
}
