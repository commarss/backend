package com.ll.commars.domain.auth.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;
	private final String credentials;

	// 인증 전
	private JwtAuthenticationToken(String accessToken) {
		super(null);
		this.principal = accessToken;
		this.credentials = accessToken;
		setAuthenticated(false);
	}

	// 인증 후
	private JwtAuthenticationToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = null;
		setAuthenticated(true);
	}

	// 인증 전 토큰 생성
	public static JwtAuthenticationToken unauthenticated(String accessToken) {
		return new JwtAuthenticationToken(accessToken);
	}

	// 인증 후 토큰 생성
	public static JwtAuthenticationToken authenticated(UserDetails principal,
		Collection<? extends GrantedAuthority> authorities) {
		return new JwtAuthenticationToken(principal, authorities);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
}
