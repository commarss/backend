package com.ll.commars.global.token.provider;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ll.commars.global.annotation.UnitTest;
import com.ll.commars.global.security.userDetails.CustomUserDetails;
import com.ll.commars.global.security.userDetails.CustomUserDetailsService;
import com.ll.commars.global.token.entity.JwtAuthenticationToken;
import com.ll.commars.global.token.entity.JwtClaims;
import com.ll.commars.global.token.entity.PrivateClaims;
import com.ll.commars.global.token.entity.PublicClaims;
import com.ll.commars.global.token.entity.TokenSubject;
import com.ll.commars.global.token.entity.TokenValue;

import io.jsonwebtoken.JwtException;

@UnitTest
@DisplayName("JwtAuthenticationProvider 테스트")
class JwtAuthenticationProviderTest {

	@InjectMocks
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private CustomUserDetailsService customUserDetailsService;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	@BeforeEach
	void setUp() {
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
	}

	@Nested
	class authenticate_메서드_테스트 {

		private final String accessToken = "valid.access.token";
		private final TokenValue accessTokenValue = TokenValue.of(accessToken);
		private final Long memberId = 1L;
		private final String memberEmail = "test@example.com";

		@Test
		void 유효한_토큰으로_인증에_성공한다() {
			// given
			given(valueOperations.get(accessToken)).willReturn(null);

			JwtClaims mockClaims = createMockClaims(memberId);
			given(jwtProvider.parseClaim(accessTokenValue)).willReturn(mockClaims);

			CustomUserDetails mockUserDetails = createMockUserDetails(memberId, memberEmail);
			given(customUserDetailsService.loadUserByUsername(String.valueOf(memberId))).willReturn(mockUserDetails);

			JwtAuthenticationToken unauthenticatedToken = JwtAuthenticationToken.unauthenticated(accessToken);

			// when
			Authentication result = jwtAuthenticationProvider.authenticate(unauthenticatedToken);

			// then
			assertAll(
				() -> assertThat(result).isInstanceOf(JwtAuthenticationToken.class),
				() -> assertThat(result.isAuthenticated()).isTrue(),
				() -> assertThat(result.getPrincipal()).isEqualTo(mockUserDetails),
				() -> assertThat(result.getAuthorities())
					.extracting(GrantedAuthority::getAuthority)
					.containsExactly("ROLE_USER")
			);

			// 각 의존성이 정확히 1번씩 호출되었는지 검증
			verify(valueOperations).get(accessToken);
			verify(jwtProvider).parseClaim(accessTokenValue);
			verify(customUserDetailsService).loadUserByUsername(String.valueOf(memberId));
		}

		@Test
		void 로그아웃된_토큰으로_인증_시_예외_발생() {
			// given
			given(valueOperations.get(accessToken)).willReturn("logout");
			JwtAuthenticationToken unauthenticatedToken = JwtAuthenticationToken.unauthenticated(accessToken);

			// when & then
			assertThatThrownBy(() -> jwtAuthenticationProvider.authenticate(unauthenticatedToken))
				.isInstanceOf(BadCredentialsException.class)
				.hasMessage("로그아웃된 토큰입니다. 다시 로그인해주세요.");
		}

		@Test
		void 유효하지_않은_토큰으로_인증_시_예외_발생() {
			// given
			given(valueOperations.get(accessToken)).willReturn(null);
			given(jwtProvider.parseClaim(accessTokenValue)).willThrow(new JwtException("Invalid token"));
			JwtAuthenticationToken unauthenticatedToken = JwtAuthenticationToken.unauthenticated(accessToken);

			// when & then
			assertThatThrownBy(() -> jwtAuthenticationProvider.authenticate(unauthenticatedToken))
				.isInstanceOf(JwtException.class)
				.hasMessage("Invalid token");
		}

		@Test
		void 존재하지_않는_사용자의_토큰으로_인증_시_예외_발생() {
			// given
			given(valueOperations.get(accessToken)).willReturn(null);
			JwtClaims mockClaims = createMockClaims(memberId);
			given(jwtProvider.parseClaim(accessTokenValue)).willReturn(mockClaims);
			given(customUserDetailsService.loadUserByUsername(String.valueOf(memberId)))
				.willThrow(new UsernameNotFoundException("User not found"));
			JwtAuthenticationToken unauthenticatedToken = JwtAuthenticationToken.unauthenticated(accessToken);

			// when & then
			assertThatThrownBy(() -> jwtAuthenticationProvider.authenticate(unauthenticatedToken))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessage("User not found");
		}
	}

	private JwtClaims createMockClaims(Long memberId) {
		PublicClaims publicClaims = new PublicClaims(
			"commars-test-issuer",
			TokenSubject.of(String.valueOf(memberId)),
			Instant.now(),
			Instant.now().plusSeconds(3600),
			UUID.randomUUID().toString()
		);
		PrivateClaims privateClaims = new PrivateClaims(
			memberId,
			Collections.singletonList("ROLE_USER")
		);
		return new JwtClaims(publicClaims, privateClaims);
	}

	private CustomUserDetails createMockUserDetails(Long memberId, String email) {
		return new CustomUserDetails(
			memberId,
			email,
			"",
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}
}
