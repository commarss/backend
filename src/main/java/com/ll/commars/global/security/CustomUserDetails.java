package com.ll.commars.global.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUserDetails extends User {

	private final Long memberId;

	public CustomUserDetails(
		Long memberId,
		String email,
		String password,
		Collection<? extends GrantedAuthority> authorities
	) {
		super(email, password, authorities);
		this.memberId = memberId;
	}
}
