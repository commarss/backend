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
		// 로그인 시점에서 username은 email
		Member member = memberRepository.findByEmail(username)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		return new CustomUserDetails(
			member.getId(),
			member.getEmail(),
			member.getPassword(),
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}
}
