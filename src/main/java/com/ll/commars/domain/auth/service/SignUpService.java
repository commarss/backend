package com.ll.commars.domain.auth.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.auth.dto.SignUpRequest;
import com.ll.commars.domain.auth.dto.SignUpResponse;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public SignUpResponse signUp(SignUpRequest signUpRequest) {
		if (memberRepository.findByEmail(signUpRequest.email()).isPresent()) {
			throw new CustomException(EMAIL_ALREADY_EXISTS);
		}

		Member member = new Member(signUpRequest.email(), passwordEncoder.encode(signUpRequest.password()), signUpRequest.name());
		Member savedMember = memberRepository.save(member);

		return new SignUpResponse(savedMember.getId(), savedMember.getEmail());
	}
}
