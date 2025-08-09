package com.ll.commars.domain.auth.token.entity;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.util.StringUtils;

import com.ll.commars.global.exception.CustomException;

public record TokenValue(String value) {

	public TokenValue {
		if (!StringUtils.hasText(value)) {
			throw new CustomException(INVALID_TOKEN);
		}
	}

	public static TokenValue of(String value) {
		return new TokenValue(value);
	}
}
