package com.ll.commars.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final int code;
	private final String message;

	public CustomException(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}

	public CustomException(ErrorCode errorCode, String detail) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage() + " : " + detail;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
