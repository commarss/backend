package com.ll.commars.global.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUserDetails extends User {

	private final Long memberId;
	private final String email; // todo: email 불필요 시 제거

	public CustomUserDetails(
		Long memberId,
		String email,
		String password,
		Collection<? extends GrantedAuthority> authorities
	) {
		super(String.valueOf(memberId), password, authorities);
		this.memberId = memberId;
		this.email = email;
	}
}
