package com.ll.commars.global.exception;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex);

		log.error("커스텀 예외 발생: {}, {}", ex.getCode(), ex.getMessage());

		return ResponseEntity
			.status(HttpStatusCode.valueOf(ex.getCode()))
			.body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
			.collect(Collectors.joining(", "));

		ErrorResponse errorResponse = new ErrorResponse(INVALID_INPUT, errorMessage);

		log.warn("유효성 검사 실패: {}", errorMessage);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse(ERROR_NOT_DEFINED, "서버 내부 오류가 발생했습니다.");
		log.error("처리되지 않은 예외 발생", ex);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(errorResponse);
	}
}
