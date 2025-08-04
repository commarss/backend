package com.ll.commars.domain.community.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.entity.PostHashTag;
import com.ll.commars.domain.community.post.repository.PostRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public Long addBoard(Long userId, String title, String content, List<PostHashTag> postHashTags, String imageUrl) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

		Post post = new Post(title, content, imageUrl, user, postHashTags);

		post = postRepository.save(post);
		return post.getId();
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
