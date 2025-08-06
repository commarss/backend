package com.ll.commars.domain.community.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.ll.commars.domain.user.entity.User;
import com.ll.commars.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final UserService userService;

	private User getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
		return userService.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(
			() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	}

	@PostMapping
	public ResponseEntity<CommentCreateResponse> createComment(
		@RequestBody CommentCreateRequest request,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		User user = getAuthenticatedUser(userDetails);
		CommentCreateResponse response = commentService.createComment(user.getId(), request);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{comment-id}")
	public ResponseEntity<CommentUpdateResponse> updateComment(
		@PathVariable("comment-id") Long commentId,
		@RequestBody CommentUpdateRequest request,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		User user = getAuthenticatedUser(userDetails);
		CommentUpdateResponse response = commentService.updateComment(user.getId(), commentId, request);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{comment-id}")
	public ResponseEntity<Void> deleteComment(
		@PathVariable("comment-id") Long commentId,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		User user = getAuthenticatedUser(userDetails);
		commentService.deleteComment(commentId, user.getId());

		return ResponseEntity.ok().build();
	}
}
