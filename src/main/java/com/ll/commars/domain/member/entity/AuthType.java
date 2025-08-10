package com.ll.commars.domain.member.entity;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.Arrays;

import com.ll.commars.global.exception.CustomException;

public enum AuthType {

	GOOGLE,
	KAKAO,
	NAVER,
	EMAIL;

	public static AuthType from(String provider) {
		return Arrays.stream(values())
			.filter(authType -> authType.name().equalsIgnoreCase(provider))
			.findFirst()
			.orElseThrow(() -> new CustomException(INVALID_OAUTH_PROVIDER, provider));
	}
}
