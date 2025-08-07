package com.ll.commars.domain.auth.token.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		long userId = Long.parseLong(username);

		return memberRepository.findById(userId)
			.map(this::createUserDetails)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
	}

	private UserDetails createUserDetails(Member member) {
		String[] roles = {"ROLE_USER"};

		return User.builder()
			.username(String.valueOf(member.getId()))
			.password("")
			.authorities(roles)
			.build();
	}
}
