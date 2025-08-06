package com.ll.commars.domain.auth.token.entity;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.util.StringUtils;

import com.ll.commars.global.exception.CustomException;

public record JwtTokenValue(String value) {

	public JwtTokenValue {
		if (!StringUtils.hasText(value)) {
			throw new CustomException(INVALID_TOKEN);
		}
	}

	public static JwtTokenValue of(String value) {
		return new JwtTokenValue(value);
	}
}
