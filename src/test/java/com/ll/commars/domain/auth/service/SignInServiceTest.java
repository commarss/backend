package com.ll.commars.domain.auth.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

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
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.annotation.IntegrationTest;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@IntegrationTest
@DisplayName("SignInService 테스트")
class SignInServiceTest {

	@Autowired
	private SignInService signInService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final String RAW_PASSWORD = "password123!";
	private static final String USER_EMAIL = "test@example.com";
	private Member member;

	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();

	@BeforeEach
	void setUp() {
		Member newMember = fixtureMonkey.giveMeBuilder(Member.class)
			.set("id", null)
			.set("email", USER_EMAIL)
			.set("password", passwordEncoder.encode(RAW_PASSWORD))
			.set("reviews", new ArrayList<>())
			.set("favorites", new ArrayList<>())
			.set("posts", new ArrayList<>())
			.set("comments", new ArrayList<>())
			.sample();

		member = memberRepository.save(newMember);
	}

	@Nested
	class 로그인_테스트 {

		@Test
		void 로그인_성공_시_토큰이_발급되고_redis에_저장된다() {
			// given
			String rawPassword = "password123!";
			SignInRequest request = new SignInRequest(member.getEmail(), rawPassword);

			// when
			SignInResponse response = signInService.signIn(request);

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
			assertThatThrownBy(() -> signInService.signIn(request))
				.isInstanceOf(BadCredentialsException.class);
		}

		@Test
		void 이메일이_틀리면_BadCredentialsException이_발생한다() {
			// given
			String nonExistentEmail = "nonexistent@example.com";
			SignInRequest request = new SignInRequest(nonExistentEmail, "any-password");

			// when & then
			assertThatThrownBy(() -> signInService.signIn(request))
				.isInstanceOf(BadCredentialsException.class);
		}
	}
}
