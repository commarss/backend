package com.ll.commars.domain.community.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.comment.dto.CommentResponse;
import com.ll.commars.domain.community.post.dto.PostDetailResponse;
import com.ll.commars.domain.community.post.dto.PostHashTagResponse;
import com.ll.commars.domain.community.post.dto.PostLikeCreateResponse;
import com.ll.commars.domain.community.post.dto.PostLikeResponse;
import com.ll.commars.domain.community.post.dto.PostListResponse;
import com.ll.commars.domain.community.post.dto.PostResponse;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.entity.PostLike;
import com.ll.commars.domain.community.post.repository.PostLikeRepository;
import com.ll.commars.domain.community.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostQueryService {

	private final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;

	@Transactional(readOnly = true)
	public PostDetailResponse getPost(Long postId) {
		Post post = postRepository.findPostWithDetailsById(postId)
			.orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

		List<CommentResponse> comments = post.getComments().stream()
			.map(CommentResponse::from)
			.toList();

		List<PostHashTagResponse> postHashTags = post.getPostHashTags().stream()
			.map(PostHashTagResponse::from)
			.toList();

		List<PostLikeCreateResponse> reactions = post.getPostLikes().stream()
			.map(PostLikeCreateResponse::from)
			.toList();

		return PostDetailResponse.of(post, comments, postHashTags, reactions);
	}

	@Transactional(readOnly = true)
	public PostListResponse getPosts() {
		List<Post> posts = postRepository.findAll();

		return PostListResponse.of(
			posts.stream()
				.map(PostResponse::of)
				.toList()
		);
	}

	@Transactional(readOnly = true)
	public PostLikeResponse getLikes(Long postId) {
		List<PostLike> postLikes = postLikeRepository.findAllByPostId(postId)
			.stream()
			.toList();

		return PostLikeResponse.from(postLikes);
	}
}
