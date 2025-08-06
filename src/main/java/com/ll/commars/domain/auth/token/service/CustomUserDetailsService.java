package com.ll.commars.domain.auth.token.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		long userId = Long.parseLong(username);

		return userRepository.findById(userId)
			.map(this::createUserDetails)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
	}

	private UserDetails createUserDetails(com.ll.commars.domain.user.entity.User user) {
		String[] roles = {"ROLE_USER"};

		return User.builder()
			.username(String.valueOf(user.getId()))
			.password("")
			.authorities(roles)
			.build();
	}
}
