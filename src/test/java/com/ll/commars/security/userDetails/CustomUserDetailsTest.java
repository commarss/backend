package com.ll.commars.security.userDetails;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ll.commars.global.annotation.UnitTest;
import com.ll.commars.global.security.userDetails.CustomUserDetails;

@UnitTest
@DisplayName("CustomUserDetails 테스트")
class CustomUserDetailsTest {

	@Nested
	class CustomUserDetails_객체_생성_테스트 {

		@Test
		void CustomUserDetails_객체를_성공적으로_생성한다() {
			// given
			Long memberId = 1L;
			String email = "test@example.com";
			String password = "password123";
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

			// when
			CustomUserDetails userDetails = new CustomUserDetails(memberId, email, password, authorities);

			// then
			assertAll(
				() -> assertThat(userDetails).isNotNull(),
				() -> assertThat(userDetails.getMemberId()).isEqualTo(memberId),
				() -> assertThat(userDetails.getEmail()).isEqualTo(email),
				() -> assertThat(userDetails.getUsername()).isEqualTo(String.valueOf(memberId)),
				() -> assertThat(userDetails.getPassword()).isEqualTo(password),
				() -> assertThat(userDetails.getAuthorities()).containsExactlyElementsOf(authorities)
			);
		}
	}
}
