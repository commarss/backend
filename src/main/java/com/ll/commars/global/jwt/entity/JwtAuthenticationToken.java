package com.ll.commars.global.jwt.entity;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final UserDetails principal;

	public JwtAuthenticationToken(UserDetails principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public UserDetails getPrincipal() {
		return principal;
	}
}
