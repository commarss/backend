package com.ll.commars.domain.auth.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.auth.client.OAuthClient;
import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.auth.dto.OAuthRequest;
import com.ll.commars.domain.auth.dto.OAuthResponse;
import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.token.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {

	private final Map<AuthType, OAuthClient> clients;
	private final MemberRepository memberRepository;
	private final TokenProvider tokenProvider;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public OAuthResponse processOAuth(AuthType authType, OAuthRequest request) {
		OAuthClient client = clients.get(authType);
		if (client == null) {
			throw new CustomException(INVALID_OAUTH_PROVIDER, authType.name());
		}

		OAuthMemberInfoDto memberInfo = client.getUserInfo(request.authorizationCode());

		return memberRepository.findByEmailAndAuthType(
				memberInfo.getEmail(),
				authType
			)
			.map(this::oAuthSignIn)
			.orElseGet(() -> oAuthSignUp(memberInfo, authType));
	}

	private OAuthResponse oAuthSignIn(Member member) {
		String accessToken = tokenProvider.generateAccessToken(member).token().value();
		String refreshToken = tokenProvider.generateRefreshToken(member).token().value();

		return new OAuthResponse(accessToken, refreshToken, false);
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

		return new OAuthResponse(accessToken, refreshToken, true);
	}
}
