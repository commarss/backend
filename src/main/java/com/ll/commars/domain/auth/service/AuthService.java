package com.ll.commars.domain.auth.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.auth.dto.OAuthRequest;
import com.ll.commars.domain.auth.dto.OAuthResponse;
import com.ll.commars.domain.auth.dto.SignInRequest;
import com.ll.commars.domain.auth.dto.SignInResponse;
import com.ll.commars.domain.auth.dto.SignUpRequest;
import com.ll.commars.domain.auth.dto.SignUpResponse;
import com.ll.commars.domain.auth.dto.TokenReissueResponse;
import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.token.provider.TokenProvider;
import com.ll.commars.global.token.entity.AccessToken;
import com.ll.commars.global.token.entity.JwtClaims;
import com.ll.commars.global.token.entity.RefreshToken;
import com.ll.commars.global.token.entity.TokenValue;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final EmailService emailService;
	private final OAuthService oAuthService;
	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;

	// todo: StringRedisTemplate vs. RedisTemplate<String, String>
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional
	public void signOut(TokenValue accessTokenValue) {
		JwtClaims claims = tokenProvider.parseClaim(accessTokenValue);

		Instant expiration = claims.publicClaims().expiresAt();
		Instant now = Instant.now();
		long remainingMillis = Duration.between(now, expiration).toMillis();

		if (remainingMillis > 0) {
			redisTemplate.opsForValue().set(
				accessTokenValue.value(),
				"logout",
				Duration.ofMillis(remainingMillis)
			);
		}
	}

	@Transactional
	public TokenReissueResponse reissueToken(String refreshTokenValueString) {
		JwtClaims claims = tokenProvider.parseClaim(TokenValue.of(refreshTokenValueString));
		Long userId = claims.privateClaims().memberId();

		String savedRefreshToken = redisTemplate.opsForValue().get("refreshToken" + userId);
		if (savedRefreshToken == null || !savedRefreshToken.equals(refreshTokenValueString)) {
			throw new CustomException(INVALID_TOKEN);
		}

		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		AccessToken newAccessToken = tokenProvider.generateAccessToken(member);
		RefreshToken newRefreshToken = tokenProvider.generateRefreshToken(member);

		redisTemplate.opsForValue().set(
			"refreshToken" + userId,
			newRefreshToken.token().value(),
			Duration.ofMillis(newRefreshToken.expiration())
		);

		return new TokenReissueResponse(
			newAccessToken.token().value(),
			newRefreshToken.token().value()
		);
	}

	@Transactional
	public void withdraw(TokenValue accessTokenValue) {
		JwtClaims claims = tokenProvider.parseClaim(accessTokenValue);
		Long userId = claims.privateClaims().memberId();

		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		memberRepository.delete(member);

		signOut(accessTokenValue);

		redisTemplate.delete("refreshToken" + userId);
	}

	public SignInResponse emailSignIn(SignInRequest request) {
		return emailService.emailSignIn(request);
	}

	public SignUpResponse emailSignUp(SignUpRequest request) {
		return emailService.emailSignUp(request);
	}

	public OAuthResponse processOAuth(AuthType authType, OAuthRequest request) {
		return oAuthService.processOAuth(authType, request);
	}
}
