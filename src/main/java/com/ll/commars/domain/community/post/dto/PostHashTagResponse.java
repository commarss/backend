package com.ll.commars.domain.community.post.dto;

import com.ll.commars.domain.community.post.entity.PostHashTag;

public record PostHashTagResponse(
	Long id,
	String name
) {

	public static PostHashTagResponse from(PostHashTag postHashTag) {
		return new PostHashTagResponse(
			postHashTag.getId(),
			postHashTag.getName()
		);
	}
}
