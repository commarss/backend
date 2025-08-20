package com.ll.commars.domain.auth.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.auth.dto.SignInRequest;
import com.ll.commars.domain.auth.dto.SignInResponse;
import com.ll.commars.domain.auth.dto.SignUpRequest;
import com.ll.commars.domain.auth.dto.SignUpResponse;
import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.token.provider.TokenProvider;
import com.ll.commars.global.token.entity.AccessToken;
import com.ll.commars.global.token.entity.RefreshToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final MemberRepository memberRepository;
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional
	public SignInResponse emailSignIn(SignInRequest request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.email(), request.password())
		);

		// todo: 불필요한 DB 조회 대신 UserDetails를 사용하도록
		Long memberId = Long.parseLong(authentication.getName());
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		AccessToken accessToken = tokenProvider.generateAccessToken(member);
		RefreshToken refreshToken = tokenProvider.generateRefreshToken(member);

		redisTemplate.opsForValue().set(
			"refreshToken" + member.getId(),
			refreshToken.token().value(),
			Duration.ofMillis(refreshToken.expiration())
		);

		return new SignInResponse(accessToken.token().value(), refreshToken.token().value(), false);
	}

	@Transactional
	public SignUpResponse emailSignUp(SignUpRequest signUpRequest) {
		if (memberRepository.findByEmail(signUpRequest.email()).isPresent()) {
			throw new CustomException(EMAIL_ALREADY_EXISTS);
		}

		Member member = new Member(
			signUpRequest.email(),
			signUpRequest.name(),
			passwordEncoder.encode(signUpRequest.password()),
			AuthType.EMAIL
		);

		Member savedMember = memberRepository.save(member);

		return new SignUpResponse(savedMember.getId(), savedMember.getEmail());
	}
}
