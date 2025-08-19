package com.ll.commars.global.token.component;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ll.commars.global.token.JwtAuthenticationToken;
import com.ll.commars.global.token.TokenProvider;
import com.ll.commars.global.token.entity.JwtClaims;
import com.ll.commars.global.token.entity.TokenValue;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;
	private final UserDetailsService userDetailsService;

	@Override
	public void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		String token = resolveToken(request);

		if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				JwtClaims claims = tokenProvider.parseClaim(TokenValue.of(token));

				String memberId = claims.publicClaims().subject().value();
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(memberId);

				JwtAuthenticationToken authentication = JwtAuthenticationToken.authenticated(
					userDetails,
					userDetails.getAuthorities()
				);

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (Exception e) {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
