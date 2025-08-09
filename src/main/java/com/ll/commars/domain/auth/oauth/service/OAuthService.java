package com.ll.commars.domain.auth.oauth.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.auth.dto.OAuthResponse;
import com.ll.commars.domain.auth.dto.OAuthSignInResponse;
import com.ll.commars.domain.auth.dto.OAuthSignUpResponse;
import com.ll.commars.domain.auth.oauth.client.OAuthClient;
import com.ll.commars.domain.auth.token.TokenProvider;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {

	private final Map<AuthType, OAuthClient> clients;
	private final MemberRepository memberRepository;
	private final TokenProvider tokenProvider;

	@Transactional
	public OAuthResponse processOAuth(AuthType authType, String authorizationCode) {
		OAuthClient client = clients.get(authType);
		if (client == null) {
			throw new CustomException(INVALID_OAUTH_PROVIDER, authType.name());
		}

		String accessToken = client.getAccessToken(authorizationCode);
		OAuthMemberInfoDto memberInfo = client.getUserInfo(accessToken);

		Optional<Member> foundMember = memberRepository.findByEmailAndAuthType(
			memberInfo.getEmail(),
			authType
		);

		return foundMember
			.map(this::oAuthSignIn)
			.orElseGet(() -> oAuthSignUp(memberInfo, authType));
	}

	private OAuthResponse oAuthSignIn(Member member) {
		String accessToken = tokenProvider.generateAccessToken(member).token().value();
		String refreshToken = tokenProvider.generateRefreshToken(member).token().value();

		return new OAuthSignInResponse(accessToken, refreshToken, false);
	}

	private OAuthResponse oAuthSignUp(OAuthMemberInfoDto memberInfo, AuthType authType) {
		Member newMember = new Member(
			memberInfo.getEmail(),
			memberInfo.getNickname(),
			authType
			);

		memberRepository.save(newMember);

		String accessToken = tokenProvider.generateAccessToken(newMember).token().value();
		String refreshToken = tokenProvider.generateRefreshToken(newMember).token().value();

		return new OAuthSignUpResponse(accessToken, refreshToken, true);
	}
}
