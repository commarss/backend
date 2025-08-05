package com.ll.commars.domain.community.comment.dto;

import com.ll.commars.domain.community.comment.entity.Comment;

public record CommentUpdateResponse(
	Long id,
	String content
) {

	public static CommentUpdateResponse from(Comment comment) {
		return new CommentUpdateResponse(
				comment.getId(),
				comment.getContent()
		);
	}
}
