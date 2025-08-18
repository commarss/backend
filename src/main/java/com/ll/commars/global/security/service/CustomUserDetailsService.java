package com.ll.commars.global.security.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.Collections;
import java.util.List;

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
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		return createCustomUserDetails(member);
	}

	private CustomUserDetails createCustomUserDetails(Member member) {
		List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

		return new CustomUserDetails(
			member.getId(),
			member.getEmail(),
			member.getPassword(),
			authorities
		);
	}
}
