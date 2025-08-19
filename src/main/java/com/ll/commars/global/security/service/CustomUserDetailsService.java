package com.ll.commars.global.security.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		long memberId = getMemberId(username);

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		return new CustomUserDetails(
			member.getId(),
			member.getEmail(),
			member.getPassword(),
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}

	private long getMemberId(String username) {
		try {
			return Long.parseLong(username);
		} catch (NumberFormatException e) {
			throw new CustomException(INVALID_TOKEN, "인증 정보가 지정된 형식과 일치하지 않습니다.");
		}
	}
}
