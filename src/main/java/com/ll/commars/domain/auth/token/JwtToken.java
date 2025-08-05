package com.ll.commars.domain.auth.token;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class JwtToken {

	private String token;
	private Long id;
	private String email;
	private Date expiration;
}
