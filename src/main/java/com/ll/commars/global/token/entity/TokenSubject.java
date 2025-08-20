package com.ll.commars.global.token.entity;

import io.jsonwebtoken.lang.Assert;

public record TokenSubject(String value) {

	public TokenSubject {
		Assert.notNull(value, "토큰 subject는 null일 수 없습니다.");
		Assert.hasText(value, "토큰 subject는 비어있을 수 없습니다.");
	}

	public static TokenSubject of(String subjectValue) {
		return new TokenSubject(subjectValue);
	}
}
