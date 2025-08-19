package com.ll.commars.global.token.entity;

import java.util.List;

public record PrivateClaims(
	Long memberId,
	List<String> roles
) {
}
