package com.ll.commars.domain.community.post.dto;

import com.ll.commars.domain.community.post.entity.PostLike;

public record PostLikeCreateResponse(
	Long id
) {

	public static PostLikeCreateResponse from(PostLike postLike) {
		return new PostLikeCreateResponse(
			postLike.getId()
		);
	}
}
