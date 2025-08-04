package com.ll.commars.domain.community.post.dto;

import java.util.List;

public record PostLikeListResponse(
	List<PostLikeResponse> reactions
) {

	public static PostLikeListResponse of(List<PostLikeResponse> reactions) {
		return new PostLikeListResponse(reactions);
	}
}
