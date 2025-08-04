package com.ll.commars.domain.community.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.post.dto.PostCreateRequest;
import com.ll.commars.domain.community.post.dto.PostCreateResponse;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.entity.PostHashTag;
import com.ll.commars.domain.community.post.repository.PostHashTagRepository;
import com.ll.commars.domain.community.post.repository.PostRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommandService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PostHashTagRepository postHashTagRepository;

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

	public List<Post> getAllBoards() {
		return postRepository.findAll();
	}

	@Transactional
	public Post getBoard(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

		post.incrementViews();
		return post;
	}

	@Transactional
	public void updateBoard(Long postId, String title, String content, List<PostHashTag> tags) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

		post.updatePost(title, content, tags);

		postRepository.save(post);
	}

	@Transactional
	public void deleteBoard(Long boardId) {
		postRepository.deleteById(boardId);
	}
}
