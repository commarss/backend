package com.ll.commars.global.token;

import com.ll.commars.global.token.entity.AccessToken;
import com.ll.commars.global.token.entity.JwtClaims;
import com.ll.commars.global.token.entity.RefreshToken;
import com.ll.commars.global.token.entity.TokenValue;
import com.ll.commars.domain.member.entity.Member;

public interface TokenProvider {

	AccessToken generateAccessToken(Member member);

	RefreshToken generateRefreshToken(Member member);

	JwtClaims parseClaim(TokenValue tokenValue);
}
