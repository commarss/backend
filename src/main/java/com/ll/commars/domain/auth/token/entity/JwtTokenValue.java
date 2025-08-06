package com.ll.commars.domain.auth.token.entity;

import org.springframework.util.StringUtils;

public record JwtTokenValue(String value) {

	public JwtTokenValue {
		if (!StringUtils.hasText(value)) {
			throw new IllegalArgumentException("Token value cannot be empty.");
		}
	}

	public static JwtTokenValue of(String value) {
		return new JwtTokenValue(value);
	}
}
