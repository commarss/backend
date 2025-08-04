package com.ll.commars.domain.community.post.dto;

import com.ll.commars.domain.community.post.entity.Post;

public record PostCreateResponse(
	Long id // 게시글 ID
) {

	public static PostCreateResponse from(Post post) {
		return new PostCreateResponse(post.getId());
	}
}
