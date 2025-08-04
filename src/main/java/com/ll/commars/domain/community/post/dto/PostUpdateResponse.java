package com.ll.commars.domain.community.post.dto;

public record PostUpdateResponse(
	Long id
) {

	public static PostUpdateResponse of(Long id) {
		return new PostUpdateResponse(
			id
		);
	}
}
