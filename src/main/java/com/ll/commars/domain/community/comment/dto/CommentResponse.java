package com.ll.commars.domain.community.comment.dto;

import com.ll.commars.domain.community.comment.entity.Comment;

public record CommentResponse(
		Long id,
		String content
) {

	public static CommentResponse from(Comment comment) {
		return new CommentResponse(
				comment.getId(),
				comment.getContent()
		);
	}
}
