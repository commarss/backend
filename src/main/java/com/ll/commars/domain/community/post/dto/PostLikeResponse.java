package com.ll.commars.domain.community.post.dto;

import java.util.List;

import com.ll.commars.domain.community.post.entity.PostLike;

public record PostLikeResponse(
	List<Long> postLikeIds
) {

	public static PostLikeResponse from(List<PostLike> postLikes) {
		List<Long> postLikeIds = postLikes.stream()
			.map(PostLike::getId)
			.toList();

		return new PostLikeResponse(postLikeIds);
	}
}
