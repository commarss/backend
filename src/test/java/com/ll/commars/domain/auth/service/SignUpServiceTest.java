package com.ll.commars.domain.auth.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ll.commars.domain.auth.dto.SignUpRequest;
import com.ll.commars.domain.auth.dto.SignUpResponse;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.MemberRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@IntegrationTest
@DisplayName("SignUpService 테스트")
class SignUpServiceTest {

	@Autowired
	private SignUpService signUpService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();

	@Test
	void 회원가입에_성공한다() {
		// given
		String email = "newuser@example.com";
		String password = "password123!";
		String name = "신규회원";
		SignUpRequest request = new SignUpRequest(email, password, name);

		// when
		SignUpResponse response = signUpService.signUp(request);

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
		String existingEmail = "existinguser@example.com";
		Member existingMember = fixtureMonkey.giveMeBuilder(Member.class)
			.set("id", null)
			.set("email", existingEmail)
			.sample();
		memberRepository.save(existingMember);

		SignUpRequest request = new SignUpRequest(existingEmail, "anypassword", "anyname");

		// when & then
		assertThatThrownBy(() -> signUpService.signUp(request))
			.isInstanceOf(CustomException.class)
			.hasMessage(EMAIL_ALREADY_EXISTS.getMessage());
	}
}
