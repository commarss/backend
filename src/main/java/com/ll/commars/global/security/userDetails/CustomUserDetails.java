package com.ll.commars.global.security.userDetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

// 관심사: 인증 객체 정보 저장
@Getter
public class CustomUserDetails extends User {

	private final Long memberId;
	private final String email;

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
