package com.ll.commars.domain.community.post.dto;

import java.util.List;

public record PostLikeCreateListResponse(
	List<PostLikeCreateResponse> reactions
) {

	public static PostLikeCreateListResponse of(List<PostLikeCreateResponse> reactions) {
		return new PostLikeCreateListResponse(reactions);
	}
}
