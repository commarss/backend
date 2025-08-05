package com.ll.commars.domain.community.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.post.dto.PostCreateRequest;
import com.ll.commars.domain.community.post.dto.PostCreateResponse;
import com.ll.commars.domain.community.post.dto.PostLikeCreateResponse;
import com.ll.commars.domain.community.post.dto.PostUpdateRequest;
import com.ll.commars.domain.community.post.dto.PostUpdateResponse;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.entity.PostHashTag;
import com.ll.commars.domain.community.post.entity.PostLike;
import com.ll.commars.domain.community.post.repository.PostRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommandService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public PostCreateResponse createPost(Long userId, PostCreateRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

		Post post = new Post(request.title(), request.content(), request.imageUrl(), user);

		List<PostHashTag> postHashTags = request.hashTags().stream()
			.map(PostHashTag::new)
			.toList();

		post.addHashTags(postHashTags);

		return PostCreateResponse.from(postRepository.save(post));
	}

	@Transactional
	public PostUpdateResponse updateBoard(Long userId, Long postId, PostUpdateRequest request) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

		// todo: 검증 로직 추가
		List<PostHashTag> postHashTags = request.hashTags().stream()
			.map(PostHashTag::new)
			.toList();

		post.updatePost(request.title(), request.content(), request.imageUrl(), postHashTags);

		return PostUpdateResponse.of(postId);
	}

	@Transactional
	public void deletePost(Long userId, Long boardId) {
		// todo: 검증 로직 추가
		postRepository.deleteById(boardId);
	}

	@Transactional
	public void incrementViews(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

		post.incrementViews();
	}

	@Transactional
	public PostLikeCreateResponse likePost(Long userId, Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

		post.addLike();

		PostLike postLike = new PostLike(post, user);
		post.getPostLikes().add(postLike);

		return PostLikeCreateResponse.from(postLike);
	}
}
