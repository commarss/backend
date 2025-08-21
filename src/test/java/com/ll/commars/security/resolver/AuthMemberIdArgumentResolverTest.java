package com.ll.commars.security.resolver;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.lang.reflect.Method;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ll.commars.global.annotation.UnitTest;
import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.security.annotation.AuthMemberId;
import com.ll.commars.global.security.resolver.AuthMemberIdArgumentResolver;
import com.ll.commars.global.security.userDetails.CustomUserDetails;

@UnitTest
@DisplayName("AuthMemberIdArgumentResolver 테스트")
class AuthMemberIdArgumentResolverTest {

	private AuthMemberIdArgumentResolver resolver;

	@BeforeEach
	void setUp() {
		resolver = new AuthMemberIdArgumentResolver();
	}

	static class DummyController {
		public void validLongMethod(@AuthMemberId Long memberId) {}
		public void validPrimitiveLongMethod(@AuthMemberId long memberId) {}
		public void invalidTypeMethod(@AuthMemberId String memberId) {}
		public void noAnnotationMethod(Long memberId) {}
	}

	@Nested
	class 파라미터_조건_테스트 {

		@Test
		void 어노테이션이_있고_Long_타입인_파라미터를_지원한다() throws NoSuchMethodException {
			// given
			MethodParameter parameter = createMethodParameter("validLongMethod", Long.class);

			// when
			boolean supports = resolver.supportsParameter(parameter);

			// then
			assertThat(supports).isTrue();
		}

		@Test
		void 어노테이션이_있고_long_타입인_파라미터를_지원한다() throws NoSuchMethodException {
			// given
			MethodParameter parameter = createMethodParameter("validPrimitiveLongMethod", long.class);

			// when
			boolean supports = resolver.supportsParameter(parameter);

			// then
			assertThat(supports).isTrue();
		}

		@Test
		void 어노테이션이_있으나_Long_타입이_아닌_파라미터는_지원하지_않는다() throws NoSuchMethodException {
			// given
			MethodParameter parameter = createMethodParameter("invalidTypeMethod", String.class);

			// when
			boolean supports = resolver.supportsParameter(parameter);

			// then
			assertThat(supports).isFalse();
		}

		@Test
		void 어노테이션이_없는_파라미터는_지원하지_않는다() throws NoSuchMethodException {
			// given
			MethodParameter parameter = createMethodParameter("noAnnotationMethod", Long.class);

			// when
			boolean supports = resolver.supportsParameter(parameter);

			// then
			assertThat(supports).isFalse();
		}
	}

	@Nested
	class 인증된_사용자_정보_조회_테스트 {

		@Test
		void 인증된_사용자의_memberId를_성공적으로_리턴한다() {
			// given
			Long expectedMemberId = 123L;
			CustomUserDetails customUserDetails = createCustomUserDetails(expectedMemberId);

			// when & then
			executeWithMockedSecurityContext(
				createAuthenticatedUser(customUserDetails),
				() -> {
					Object resolvedArgument = resolver.resolveArgument(null, null, null, null);
					assertThat(resolvedArgument).isEqualTo(expectedMemberId);
				}
			);
		}

		@Test
		void 인증_결과가_null이라면_예외가_발생한다() {
			// when & then
			executeWithMockedSecurityContext(
				null,
				() -> assertThatThrownBy(() -> resolver.resolveArgument(null, null, null, null))
					.isInstanceOf(CustomException.class)
					.hasMessage(MEMBER_NOT_AUTHENTICATED.getMessage())
			);
		}

		@Test
		void 인증되지_않는_사용자라면_예외가_발생한다() {
			// when & then
			executeWithMockedSecurityContext(
				createUnauthenticatedUser(),
				() -> assertThatThrownBy(() -> resolver.resolveArgument(null, null, null, null))
					.isInstanceOf(CustomException.class)
					.hasMessage(MEMBER_NOT_AUTHENTICATED.getMessage())
			);
		}

		@Test
		void Principal이_CustomUserDetails_타입이_아니라면_예외가_발생한다() {
			// when & then
			executeWithMockedSecurityContext(
				createAuthenticatedUserWithInvalidPrincipal(),
				() -> assertThatThrownBy(() -> resolver.resolveArgument(null, null, null, null))
					.isInstanceOf(CustomException.class)
					.hasMessage(MEMBER_NOT_AUTHENTICATED.getMessage())
			);
		}
	}

	private MethodParameter createMethodParameter(String methodName, Class<?> parameterType) throws NoSuchMethodException {
		Method method = DummyController.class.getMethod(methodName, parameterType);
		return new MethodParameter(method, 0);
	}

	// SecurityContextHolder를 모킹하고 테스트 로직 수행
	private void executeWithMockedSecurityContext(Authentication authentication, Runnable testLogic) {
		try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
			SecurityContext securityContext = mock(SecurityContext.class);
			given(securityContext.getAuthentication()).willReturn(authentication);
			mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

			testLogic.run();
		}
	}

	private Authentication createAuthenticatedUser(CustomUserDetails customUserDetails) {
		Authentication authentication = mock(Authentication.class);
		given(authentication.isAuthenticated()).willReturn(true);
		given(authentication.getPrincipal()).willReturn(customUserDetails);
		return authentication;
	}

	private Authentication createUnauthenticatedUser() {
		Authentication authentication = mock(Authentication.class);
		given(authentication.isAuthenticated()).willReturn(false);
		return authentication;
	}

	private Authentication createAuthenticatedUserWithInvalidPrincipal() {
		Authentication authentication = mock(Authentication.class);
		given(authentication.isAuthenticated()).willReturn(true);
		given(authentication.getPrincipal()).willReturn("anonymousUser");
		return authentication;
	}

	// 복잡성 때문에 FixtureMonkey 대신 생성자로 CustomUserDetails를 생성
	private CustomUserDetails createCustomUserDetails(Long memberId) {
		return new CustomUserDetails(
			memberId,
			"test@example.com",
			"password",
			Collections.emptyList()
		);
	}
}
