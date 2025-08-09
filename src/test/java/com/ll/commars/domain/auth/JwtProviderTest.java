package com.ll.commars.domain.auth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.ll.commars.domain.auth.token.JwtProperties;
import com.ll.commars.domain.auth.token.component.JwtProvider;
import com.ll.commars.domain.auth.token.entity.AccessToken;
import com.ll.commars.domain.auth.token.entity.JwtClaims;
import com.ll.commars.domain.auth.token.entity.RefreshToken;
import com.ll.commars.domain.auth.token.entity.TokenSubject;
import com.ll.commars.domain.auth.token.entity.TokenValue;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.global.annotation.UnitTest;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@UnitTest
@DisplayName("JwtProvider 테스트")
class JwtProviderTest {

	private JwtProvider jwtProvider;

	@Mock
	private JwtProperties jwtProperties;

	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();

	private static final String MOCK_SECRET_KEY = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
	private static final long MOCK_ACCESS_TOKEN_EXPIRATION_MS = Duration.ofHours(1).toMillis(); // 3600000L
	private static final long MOCK_REFRESH_TOKEN_EXPIRATION_MS = Duration.ofDays(7).toMillis(); // 604800000L

	private Member member;

	@BeforeEach
	void setUp() {
		when(jwtProperties.key()).thenReturn(MOCK_SECRET_KEY);
		jwtProvider = new JwtProvider(jwtProperties);

		member = fixtureMonkey.giveMeBuilder(Member.class)
			.set("id", 1L)
			.set("email", "test@example.com")
			.sample();
	}

	@Nested
	class 토큰_생성_및_파싱_성공_테스트 {

		@Test
		void AccessToken을_성공적으로_생성하고_파싱한다() {
			// given
			when(jwtProperties.accessTokenExpiration()).thenReturn(MOCK_ACCESS_TOKEN_EXPIRATION_MS);

			// when
			AccessToken accessToken = jwtProvider.generateAccessToken(member);
			JwtClaims claims = jwtProvider.parseClaim(accessToken.token());

			// then
			assertAll(
				() -> assertThat(accessToken).isNotNull(),
				() -> assertThat(accessToken.subject()).isEqualTo(TokenSubject.of(member.getEmail())),
				() -> assertThat(accessToken.expiration()).isEqualTo(MOCK_ACCESS_TOKEN_EXPIRATION_MS),
				() -> assertThat(claims.privateClaims().userId()).isEqualTo(member.getId())
			);
		}

		@Test
		void RefreshToken을_성공적으로_생성하고_파싱한다() {
			// given
			when(jwtProperties.refreshTokenExpiration()).thenReturn(MOCK_REFRESH_TOKEN_EXPIRATION_MS);

			// when
			RefreshToken refreshToken = jwtProvider.generateRefreshToken(member);
			JwtClaims claims = jwtProvider.parseClaim(refreshToken.token());

			// then
			assertAll(
				() -> assertThat(refreshToken.subject().value()).isEqualTo(member.getEmail()),
				() -> assertThat(refreshToken.expiration()).isEqualTo(MOCK_REFRESH_TOKEN_EXPIRATION_MS),
				() -> assertThat(claims.privateClaims().userId()).isEqualTo(member.getId()),
				() -> assertThat(claims.privateClaims().roles()).isNullOrEmpty()
			);
		}
	}

	@Nested
	class 토큰_파싱_실패_테스트 {

		@Test
		void 만료된_토큰을_파싱하면_ExpiredJwtException이_발생한다() {
			// given
			when(jwtProperties.accessTokenExpiration()).thenReturn(-1000L);
			AccessToken expiredToken = jwtProvider.generateAccessToken(member);

			// when & then
			assertThatThrownBy(() -> jwtProvider.parseClaim(expiredToken.token()))
				.isInstanceOf(ExpiredJwtException.class);
		}

		@Test
		void 잘못된_형식의_토큰을_파싱하면_MalformedJwtException이_발생한다() {
			// given
			TokenValue malformedToken = TokenValue.of("this.is.malformed.token");

			// when & then
			assertThatThrownBy(() -> jwtProvider.parseClaim(malformedToken))
				.isInstanceOf(MalformedJwtException.class);
		}

		@Test
		void 잘못된_서명을_가진_토큰을_파싱하면_SignatureException이_발생한다() {
			// given
			JwtProperties invalidProperties = mock(JwtProperties.class);
			when(invalidProperties.key()).thenReturn(
				"this-is-an-entirely-different-and-invalid-secret-key-12345-12345-12345-12345-12345");
			JwtProvider invalidJwtProvider = new JwtProvider(invalidProperties);

			when(jwtProperties.accessTokenExpiration()).thenReturn(MOCK_ACCESS_TOKEN_EXPIRATION_MS);
			AccessToken token = jwtProvider.generateAccessToken(member);

			// when & then
			assertThatThrownBy(() -> invalidJwtProvider.parseClaim(token.token()))
				.isInstanceOf(SignatureException.class);
		}
	}
}
