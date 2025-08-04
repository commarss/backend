package com.ll.commars.domain.community.post.dto;

import com.ll.commars.domain.community.post.entity.PostLike;

public record PostLikeResponse(
	Long id
) {

	public static PostLikeResponse from(PostLike postLike) {
		return new PostLikeResponse(
			postLike.getId()
		);
	}
}
