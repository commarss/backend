package com.ll.commars.domain.community.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.community.comment.dto.CommentCreateRequest;
import com.ll.commars.domain.community.comment.dto.CommentCreateResponse;
import com.ll.commars.domain.community.comment.dto.CommentUpdateRequest;
import com.ll.commars.domain.community.comment.dto.CommentUpdateResponse;
import com.ll.commars.domain.community.comment.service.CommentService;
import com.ll.commars.global.security.annotation.AuthMemberId;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommentCreateResponse> createComment(
		@AuthMemberId long memberId,
		@RequestBody CommentCreateRequest request
	) {
		CommentCreateResponse response = commentService.createComment(memberId, request);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{comment-id}")
	public ResponseEntity<CommentUpdateResponse> updateComment(
		@PathVariable("comment-id") Long commentId,
		@AuthMemberId long memberId,
		@RequestBody CommentUpdateRequest request
	) {
		CommentUpdateResponse response = commentService.updateComment(memberId, commentId, request);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{comment-id}")
	public ResponseEntity<Void> deleteComment(
		@PathVariable("comment-id") Long commentId,
		@AuthMemberId long memberId
	) {
		commentService.deleteComment(commentId, memberId);

		return ResponseEntity.ok().build();
	}
}
