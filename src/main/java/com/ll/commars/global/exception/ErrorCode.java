package com.ll.commars.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	;

	private final int code;
	private final String message;
}
