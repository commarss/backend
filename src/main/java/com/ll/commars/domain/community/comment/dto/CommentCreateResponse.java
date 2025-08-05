package com.ll.commars.domain.community.comment.dto;

import com.ll.commars.domain.community.comment.entity.Comment;

public record CommentCreateResponse(
	Long id, // 댓글 ID
	String content // 댓글 내용
) {

	public static CommentCreateResponse from(Comment comment) {
		return new CommentCreateResponse(
			comment.getId(),
			comment.getContent()
		);
	}
}
