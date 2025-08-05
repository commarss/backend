package com.ll.commars.domain.community.comment.dto;

import java.util.List;

public record CommentListResponse(
	List<CommentResponse> comments
) {

	public static CommentListResponse of(List<CommentResponse> comments) {
		return new CommentListResponse(comments);
	}
}
