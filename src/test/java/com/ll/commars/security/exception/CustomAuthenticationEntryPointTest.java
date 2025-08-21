package com.ll.commars.security.exception;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.commars.global.annotation.UnitTest;
import com.ll.commars.global.security.exception.CustomAuthenticationEntryPoint;

@UnitTest
@DisplayName("CustomAuthenticationEntryPoint 테스트")
class CustomAuthenticationEntryPointTest {

	private CustomAuthenticationEntryPoint authenticationEntryPoint;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		authenticationEntryPoint = new CustomAuthenticationEntryPoint();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		objectMapper = new ObjectMapper();
	}

	@Test
	void 상태_코드와_에러_응답을_올바르게_설정한다() throws IOException {
		// given
		AuthenticationException authException = new AuthenticationException("인증 실패") {};

		// when
		authenticationEntryPoint.commence(request, response, authException);

		// then
		String responseBody = response.getContentAsString();
		Map<String, Object> errorDetails = objectMapper.readValue(responseBody, new TypeReference<>() {});

		assertAll(
			() -> assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
			() -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"),
			() -> assertThat(response.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name()),
			() -> assertThat(errorDetails).containsEntry("error", "Unauthorized"),
			() -> assertThat(errorDetails).containsEntry("message", "인증이 필요합니다. 유효한 토큰을 포함하여 요청해주세요.")
		);
	}
}
