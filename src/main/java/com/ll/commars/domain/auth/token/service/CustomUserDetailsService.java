package com.ll.commars.domain.auth.token.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		return memberRepository.findByEmail(email)
			.map(this::createUserDetails)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}

	private UserDetails createUserDetails(Member member) {
		String[] roles = {"ROLE_USER"};

		return User.builder()
			.username(member.getEmail())
			.password(member.getPassword())
			.authorities(roles)
			.build();
	}
}
