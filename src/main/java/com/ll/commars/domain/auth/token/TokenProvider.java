package com.ll.commars.domain.auth.token;

import com.ll.commars.domain.auth.token.entity.AccessToken;
import com.ll.commars.domain.auth.token.entity.JwtClaims;
import com.ll.commars.domain.auth.token.entity.JwtTokenValue;
import com.ll.commars.domain.auth.token.entity.RefreshToken;
import com.ll.commars.domain.member.entity.Member;

public interface TokenProvider {

	AccessToken generateAccessToken(Member member);

	RefreshToken generateRefreshToken(Member member);

	JwtClaims parseClaim(JwtTokenValue tokenValue);
}
