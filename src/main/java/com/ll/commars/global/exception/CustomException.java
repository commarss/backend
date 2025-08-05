package com.ll.commars.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final int code;
	private final String message;

	public CustomException(ErrorCode errorCode) {
		// todo: super()을 통해 부모 클래스에 메시지를 전달하는 방법을 선택할지. 이 경우 message 필드 불필요
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}

	public CustomException(ErrorCode errorCode, String detail) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage() + " : " + detail;
	}
}
