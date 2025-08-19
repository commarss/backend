package com.ll.commars.global.token;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ll.commars.global.security.service.CustomUserDetailsService;
import com.ll.commars.global.token.component.JwtProvider;
import com.ll.commars.global.token.entity.JwtClaims;
import com.ll.commars.global.token.entity.TokenValue;

import lombok.RequiredArgsConstructor;

// JwtAuthenticationToken의 인증을 처리
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtProvider jwtProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String accessToken = (String) authentication.getPrincipal();
		JwtClaims claims = jwtProvider.parseClaim(TokenValue.of(accessToken));
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.publicClaims().subject().value());

		return JwtAuthenticationToken.authenticated(userDetails, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
