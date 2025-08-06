package com.ll.commars.domain.auth.token;

import com.ll.commars.domain.auth.token.entity.AccessToken;
import com.ll.commars.domain.auth.token.entity.JwtClaims;
import com.ll.commars.domain.auth.token.entity.RefreshToken;
import com.ll.commars.domain.user.entity.User;

public interface TokenProvider {

	AccessToken generateAccessToken(User user);
	RefreshToken generateRefreshToken(User user);
	boolean validateToken(String tokenValue);
	JwtClaims getClaims(String tokenValue);
}
