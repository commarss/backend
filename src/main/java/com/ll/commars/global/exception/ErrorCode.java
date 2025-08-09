package com.ll.commars.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	// Token
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),

	// Authentication
	EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 이메일입니다."),

	// Member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 회원을 찾을 수 없습니다."),
	;

	private final int code;
	private final String message;
}
