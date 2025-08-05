package com.ll.commars.global.exception;

public record ErrorResponse(
	String message
) {

	public ErrorResponse(CustomException e) {
		this(e.getMessage());
	}
}
