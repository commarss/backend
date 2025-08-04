package com.ll.commars.domain.auth.auth.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class AuthResponse {

	private String accessToken;
	private AuthUser authUser;
}
