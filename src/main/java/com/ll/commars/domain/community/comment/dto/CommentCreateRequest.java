package com.ll.commars.domain.community.comment.dto;

public record CommentCreateRequest(
	Long id, // 게시글 ID
	String content
) {
}
