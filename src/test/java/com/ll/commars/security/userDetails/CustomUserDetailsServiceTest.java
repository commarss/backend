package com.ll.commars.security.userDetails;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.fixture.MemberFixture;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.security.userDetails.CustomUserDetails;
import com.ll.commars.global.security.userDetails.CustomUserDetailsService;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@IntegrationTest
@DisplayName("CustomUserDetailsService 테스트")
class CustomUserDetailsServiceTest {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private MemberRepository memberRepository;

	private Member member;

	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();

	@BeforeEach
	void setUp() {
		MemberFixture memberFixture = new MemberFixture(fixtureMonkey, memberRepository);

		member = memberFixture.사용자("test@email.com", "password", AuthType.EMAIL);
	}

	@Nested
	class loadUserByUsername_메서드_테스트 {

		@Test
		void 숫자_형식의_username으로_사용자를_조회한다() {
			// given
			Long memberId = member.getId();
			String username = String.valueOf(memberId);

			// when
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

			// then
			assertAll(
				() -> assertThat(userDetails).isInstanceOf(CustomUserDetails.class),
				() -> assertThat(userDetails.getUsername()).isEqualTo(username),
				() -> assertThat(((CustomUserDetails)userDetails).getEmail()).isEqualTo(member.getEmail())
			);
		}

		@Test
		void 문자열_형식의_username으로_사용자를_조회한다() {
			// given
			String email = member.getEmail();

			// when
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

			// then
			assertAll(
				() -> assertThat(userDetails).isInstanceOf(CustomUserDetails.class),
				() -> assertThat(((CustomUserDetails)userDetails).getEmail()).isEqualTo(email),
				() -> assertThat(userDetails.getUsername()).isEqualTo(String.valueOf(member.getId()))
			);
		}

		@Test
		void 존재하지_않는_memberId로_조회_시_예외가_발생한다() {
			// given
			Long invalidMemberId = 999L;
			String username = String.valueOf(invalidMemberId);

			// when & then
			assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(username))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessage("ID " + invalidMemberId + "에 해당하는 사용자를 찾을 수 없습니다.");
		}

		@Test
		void 존재하지_않는_email로_조회_시_예외가_발생한다() {
			// given
			String invalidEmail = "invalid@example.com";

			// when & then
			assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(invalidEmail))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessage("이메일 " + invalidEmail + "에 해당하는 사용자를 찾을 수 없습니다.");
		}
	}
}
