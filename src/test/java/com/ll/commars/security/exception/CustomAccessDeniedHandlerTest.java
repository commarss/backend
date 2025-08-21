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
import org.springframework.security.access.AccessDeniedException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.commars.global.annotation.UnitTest;
import com.ll.commars.global.security.exception.CustomAccessDeniedHandler;

@UnitTest
@DisplayName("CustomAccessDeniedHandler 테스트")
class CustomAccessDeniedHandlerTest {

	private CustomAccessDeniedHandler customAccessDeniedHandler;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		customAccessDeniedHandler = new CustomAccessDeniedHandler();

		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();

		objectMapper = new ObjectMapper();
	}

	@Test
	void 상태_코드와_에러_응답을_올바르게_설정한다() throws IOException {
		// given
		AccessDeniedException accessDeniedException = new AccessDeniedException("접근이 거부되었습니다.");

		// when
		customAccessDeniedHandler.handle(request, response, accessDeniedException);

		// then
		String responseBody = response.getContentAsString(StandardCharsets.UTF_8);
		Map<String, Object> errorDetails = objectMapper.readValue(responseBody, new TypeReference<>() {});

		assertAll(
			() -> assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value()),
			() -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE + ";charset=" + StandardCharsets.UTF_8.name()),
			() -> assertThat(response.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name()),
			() -> assertThat(errorDetails).containsEntry("error", "Forbidden"),
			() -> assertThat(errorDetails).containsEntry("message", "이 리소스에 접근할 권한이 없습니다.")
		);
	}
}
