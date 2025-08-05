package com.ll.commars.domain.community.post.dto;

import com.ll.commars.domain.community.post.entity.Post;

public record PostResponse(
	Long id,
	String title
) {

	public static PostResponse of(Post post) {
		return new PostResponse(
			post.getId(),
			post.getTitle()
		);
	}
}
