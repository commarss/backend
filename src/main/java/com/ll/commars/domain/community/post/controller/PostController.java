package com.ll.commars.domain.community.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.community.post.dto.PostCreateRequest;
import com.ll.commars.domain.community.post.dto.PostCreateResponse;
import com.ll.commars.domain.community.post.dto.PostDetailResponse;
import com.ll.commars.domain.community.post.dto.PostLikeCreateResponse;
import com.ll.commars.domain.community.post.dto.PostLikeResponse;
import com.ll.commars.domain.community.post.dto.PostListResponse;
import com.ll.commars.domain.community.post.dto.PostUpdateRequest;
import com.ll.commars.domain.community.post.dto.PostUpdateResponse;
import com.ll.commars.domain.community.post.service.PostCommandService;
import com.ll.commars.domain.community.post.service.PostQueryService;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostCommandService postCommandService;
	private final PostQueryService postQueryService;
	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<PostCreateResponse> createPost(
		@RequestBody PostCreateRequest request,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Member member = getAuthenticatedUser(userDetails);
		PostCreateResponse response = postCommandService.createPost(member.getId(), request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{post-id}")
	public ResponseEntity<PostDetailResponse> getPost(
		@PathVariable("post-id") Long postId
	) {
		postCommandService.incrementViews(postId);
		PostDetailResponse response = postQueryService.getPost(postId);
		return ResponseEntity.ok(response);
	}

	// todo: paging 적용
	@GetMapping
	public ResponseEntity<PostListResponse> getPosts() {
		PostListResponse response = postQueryService.getPosts();
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{post-id}")
	public ResponseEntity<PostUpdateResponse> updatePost(
		@PathVariable("post-id") Long postId,
		@RequestBody PostUpdateRequest request,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Member member = getAuthenticatedUser(userDetails);
		PostUpdateResponse response = postCommandService.updatePost(member.getId(), postId, request);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{post-id}")
	public ResponseEntity<Void> deletePost(
		@PathVariable("post-id") Long postId,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Member member = getAuthenticatedUser(userDetails);
		postCommandService.deletePost(member.getId(), postId);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/{post-id}/like")
	public ResponseEntity<PostLikeCreateResponse> likePost(
		@PathVariable("post-id") Long postId,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Member member = getAuthenticatedUser(userDetails);

		PostLikeCreateResponse response = postCommandService.likePost(member.getId(), postId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{post-id}/like")
	public ResponseEntity<PostLikeResponse> getLikes(
		@PathVariable("post-id") Long postId
	) {
		PostLikeResponse response = postQueryService.getLikes(postId);

		return ResponseEntity.ok(response);
	}

	private Member getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
		return memberService.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(
			() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	}
}