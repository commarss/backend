package com.ll.commars.domain.auth.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ll.commars.domain.auth.dto.SignInRequest;
import com.ll.commars.domain.auth.dto.SignInResponse;
import com.ll.commars.domain.auth.token.TokenProvider;
import com.ll.commars.domain.auth.token.entity.AccessToken;
import com.ll.commars.domain.auth.token.entity.RefreshToken;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.MemberRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignInService {

	private final MemberRepository memberRepository;
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final RedisTemplate<String, String> redisTemplate;

	public SignInResponse signIn(SignInRequest request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.email(), request.password())
		);

		String email = authentication.getName();
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		AccessToken accessToken = tokenProvider.generateAccessToken(member);
		RefreshToken refreshToken = tokenProvider.generateRefreshToken(member);

		redisTemplate.opsForValue().set(
			"refreshToken" + member.getId(),
			refreshToken.token().value(),
			Duration.ofMillis(refreshToken.expiration())
		);

		return new SignInResponse(accessToken.token().value(), refreshToken.token().value());
	}
}
