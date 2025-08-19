package com.ll.commars.global.security.service;

import java.util.Collections;

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

// username을 기반으로 사용자 정보를 조회
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// username은 email
		Member member = memberRepository.findByEmailAndAuthType(username, AuthType.EMAIL)
			.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없거나 이메일/비밀번호가 일치하지 않습니다."));

		return new CustomUserDetails(
			member.getId(),
			member.getEmail(),
			member.getPassword(),
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}
}
