package com.ll.commars.domain.community.post.dto;

import java.util.List;

public record PostListResponse(
	List<PostResponse> posts
) {

	public static PostListResponse of(List<PostResponse> posts) {
		return new PostListResponse(posts);
	}
}
