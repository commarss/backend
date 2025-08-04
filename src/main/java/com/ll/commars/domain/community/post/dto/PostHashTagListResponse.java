package com.ll.commars.domain.community.post.dto;

import java.util.List;

public record PostHashTagListResponse(
	List<PostHashTagResponse> postHashTags
) {

	public static PostHashTagListResponse of(List<PostHashTagResponse> postHashTags) {
		return new PostHashTagListResponse(postHashTags);
	}
}
