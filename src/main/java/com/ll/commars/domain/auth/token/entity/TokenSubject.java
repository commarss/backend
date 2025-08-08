package com.ll.commars.domain.auth.token.entity;

import java.util.regex.Pattern;

import io.jsonwebtoken.lang.Assert;

public record TokenSubject(String value) {

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
		"^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
	);

	public TokenSubject {
		Assert.notNull(value, "토큰 subject는 null일 수 없습니다.");
		Assert.isTrue(EMAIL_PATTERN.matcher(value).matches(), "유효하지 않은 이메일 형식입니다: " + value);
	}

	public static TokenSubject of(String email) {
		return new TokenSubject(email);
	}
}
