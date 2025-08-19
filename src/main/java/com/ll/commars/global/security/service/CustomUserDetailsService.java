package com.ll.commars.global.security.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

// 관심사: username을 기반으로 사용자 정보를 조회
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member;
		if (isNumeric(username)) {
			// JWT 인증, memberId로 사용자 조회
			long memberId = Long.parseLong(username);
			member = memberRepository.findById(memberId)
				.orElseThrow(() -> new UsernameNotFoundException("ID " + memberId + "에 해당하는 사용자를 찾을 수 없습니다."));
		} else {
			// 이메일 로그인, 이메일로 사용자 조회
			member = memberRepository.findByEmailAndAuthType(username, AuthType.EMAIL)
				.orElseThrow(() -> new UsernameNotFoundException("이메일 " + username + "에 해당하는 사용자를 찾을 수 없습니다."));
		}

		String password = Optional.ofNullable(member.getPassword()).orElse("");

		return new CustomUserDetails(
			member.getId(),
			member.getEmail(),
			password,
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}

	private boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
