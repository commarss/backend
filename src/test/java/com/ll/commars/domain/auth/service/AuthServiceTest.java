package com.ll.commars.domain.auth.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ll.commars.domain.auth.dto.SignInRequest;
import com.ll.commars.domain.auth.dto.SignInResponse;
import com.ll.commars.domain.auth.dto.SignUpRequest;
import com.ll.commars.domain.auth.dto.SignUpResponse;
import com.ll.commars.domain.auth.dto.TokenReissueResponse;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.fixture.MemberFixture;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.token.entity.AccessToken;
import com.ll.commars.global.token.entity.RefreshToken;
import com.ll.commars.global.token.entity.TokenValue;
import com.ll.commars.global.token.provider.TokenProvider;

import io.jsonwebtoken.JwtException;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@IntegrationTest
@DisplayName("AuthService 테스트")
class AuthServiceTest {

	@Autowired
	private AuthService authService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private MemberFixture memberFixture;

	private Member member;

	private static final String USER_EMAIL = "test@example.com";
	private static final String RAW_PASSWORD = "password123!";

	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();

	@BeforeEach
	void setUp() {
		memberFixture = new MemberFixture(fixtureMonkey, memberRepository);

		String encodedPassword = passwordEncoder.encode(RAW_PASSWORD);
		member = memberFixture.이메일_사용자(USER_EMAIL, encodedPassword);
	}

	@Nested
	class 이메일_회원가입_테스트 {

		@Test
		void 회원가입에_성공한다() {
			// given
			String email = "newuser@example.com";
			String password = "password123!";
			String name = "신규회원";
			SignUpRequest request = new SignUpRequest(email, password, name);

			// when
			SignUpResponse response = authService.emailSignUp(request);

			// then
			assertThat(response).isNotNull();
			assertThat(response.email()).isEqualTo(email);

			Member foundMember = memberRepository.findByEmail(email)
				.orElseThrow(() -> new AssertionError("회원이 데이터베이스에 저장되지 않았습니다."));

			assertThat(foundMember.getName()).isEqualTo(name);
			assertThat(passwordEncoder.matches(password, foundMember.getPassword())).isTrue();
		}

		@Test
		void 이미_존재하는_이메일로_회원가입_시_CustomException이_발생한다() {
			// given
			SignUpRequest request = new SignUpRequest(USER_EMAIL, "anypassword", "anyname");

			// when & then
			assertThatThrownBy(() -> authService.emailSignUp(request))
				.isInstanceOf(CustomException.class)
				.hasMessage(EMAIL_ALREADY_EXISTS.getMessage());
		}
	}

	@Nested
	class 이메일_로그인_테스트 {

		@Test
		void 로그인_성공_시_토큰이_발급되고_redis에_저장된다() {
			// given
			SignInRequest request = new SignInRequest(USER_EMAIL, RAW_PASSWORD);

			// when
			SignInResponse response = authService.emailSignIn(request);

			// then
			assertThat(response).isNotNull();
			assertThat(response.accessToken()).isNotBlank();
			assertThat(response.refreshToken()).isNotBlank();

			String redisKey = "refreshToken" + member.getId();
			String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);

			assertThat(storedRefreshToken).isEqualTo(response.refreshToken());
		}

		@Test
		void 비밀번호가_틀리면_BadCredentialsException이_발생한다() {
			// given
			String wrongPassword = "wrong-password";
			SignInRequest request = new SignInRequest(member.getEmail(), wrongPassword);

			// when & then
			assertThatThrownBy(() -> authService.emailSignIn(request))
				.isInstanceOf(BadCredentialsException.class);
		}

		@Test
		void 이메일이_틀리면_BadCredentialsException이_발생한다() {
			// given
			String nonExistentEmail = "nonexistent@example.com";
			SignInRequest request = new SignInRequest(nonExistentEmail, "any-password");

			// when & then
			assertThatThrownBy(() -> authService.emailSignIn(request))
				.isInstanceOf(BadCredentialsException.class);
		}
	}

	@Nested
	class 로그아웃_테스트 {

		@Test
		void 로그아웃_시_AccessToken이_Redis에_저장되어_무효화된다() {
			// given
			AccessToken accessToken = tokenProvider.generateAccessToken(member);
			TokenValue accessTokenValue = accessToken.token();

			// when
			authService.signOut(accessTokenValue);

			// then
			String storedValue = redisTemplate.opsForValue().get(accessTokenValue.value());
			Long expire = redisTemplate.getExpire(accessTokenValue.value(), TimeUnit.MILLISECONDS);

			assertThat(storedValue).isEqualTo("logout");
			assertThat(expire).isNotNull();
			assertThat(expire).isGreaterThan(0);
			assertThat(expire).isCloseTo(accessToken.expiration(), within(1000L));
		}
	}

	@Nested
	class 토큰_재발급_테스트 {

		private RefreshToken refreshToken;
		private String refreshTokenValue;
		private String redisKey;

		@Test
		void 유효한_RefreshToken으로_토큰_재발급에_성공한다() {
			// given
			refreshToken = tokenProvider.generateRefreshToken(member);
			refreshTokenValue = refreshToken.token().value();
			redisKey = "refreshToken" + member.getId();
			redisTemplate.opsForValue().set(redisKey, refreshTokenValue, Duration.ofMillis(refreshToken.expiration()));

			// when
			TokenReissueResponse response = authService.reissueToken(refreshTokenValue);

			// then
			assertThat(response).isNotNull();
			assertThat(response.accessToken()).isNotBlank();
			assertThat(response.refreshToken()).isNotBlank();
			assertThat(response.refreshToken()).isNotEqualTo(refreshTokenValue); // 새로운 RefreshToken이 발급되었는지

			String newSavedRefreshToken = redisTemplate.opsForValue().get(redisKey);
			assertThat(newSavedRefreshToken).isEqualTo(response.refreshToken());
		}

		@Test
		void Redis에_저장되지_않은_RefreshToken으로_재발급_요청_시_예외가_발생한다() {
			// given
			refreshToken = tokenProvider.generateRefreshToken(member);
			refreshTokenValue = refreshToken.token().value();

			// when & then
			assertThatThrownBy(() -> authService.reissueToken(refreshTokenValue))
				.isInstanceOf(CustomException.class)
				.hasMessage(INVALID_TOKEN.getMessage());
		}

		@Test
		void 위조된_RefreshToken으로_재발급_요청_시_예외가_발생한다() {
			// given
			String malformedToken = "this-is-not-a-valid-jwt-token";

			// when & then
			assertThatThrownBy(() -> authService.reissueToken(malformedToken))
				.isInstanceOf(JwtException.class);
		}

		@Test
		void 토큰_재발급_후_이전_RefreshToken은_더_이상_유효하지_않다() {
			// given
			refreshToken = tokenProvider.generateRefreshToken(member);
			refreshTokenValue = refreshToken.token().value();
			redisKey = "refreshToken" + member.getId();
			redisTemplate.opsForValue().set(redisKey, refreshTokenValue, Duration.ofMillis(refreshToken.expiration()));

			// when
			TokenReissueResponse first = authService.reissueToken(refreshTokenValue);

			// then
			assertThatThrownBy(() -> authService.reissueToken(refreshTokenValue))
				.isInstanceOf(CustomException.class)
				.hasMessage(INVALID_TOKEN.getMessage());

			assertThat(redisTemplate.opsForValue().get(redisKey)).isEqualTo(first.refreshToken());
		}
	}

	@Nested
	class 회원_탈퇴_테스트 {

		@Test
		void 회원_탈퇴_시_DB와_Redis에서_관련_정보가_모두_삭제된다() {
			// given
			AccessToken accessToken = tokenProvider.generateAccessToken(member);
			RefreshToken refreshToken = tokenProvider.generateRefreshToken(member);
			String refreshTokenRedisKey = "refreshToken" + member.getId();
			redisTemplate.opsForValue().set(refreshTokenRedisKey, refreshToken.token().value());

			// when
			authService.withdraw(accessToken.token());

			// then
			Optional<Member> foundMember = memberRepository.findById(member.getId());
			assertThat(foundMember).isEmpty();

			Boolean hasRefreshToken = redisTemplate.hasKey(refreshTokenRedisKey);
			assertThat(hasRefreshToken).isFalse();

			String loggedOutAccessToken = redisTemplate.opsForValue().get(accessToken.token().value());
			assertThat(loggedOutAccessToken).isEqualTo("logout");
		}
	}
}
