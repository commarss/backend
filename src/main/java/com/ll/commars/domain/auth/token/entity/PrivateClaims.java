package com.ll.commars.domain.auth.token.entity;

import java.util.List;

public record PrivateClaims(
	Long userId,
	List<String> roles
) {}
