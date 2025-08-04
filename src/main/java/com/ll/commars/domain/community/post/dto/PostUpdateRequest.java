package com.ll.commars.domain.community.post.dto;

import java.util.List;

public record PostUpdateRequest(
	String title,
	String content,
	String imageUrl,
	List<String> hashTags
) {
}
