package com.ll.commars.domain.community.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.comment.dto.CommentResponse;
import com.ll.commars.domain.community.post.dto.PostHashTagResponse;
import com.ll.commars.domain.community.post.dto.PostResponse;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.repository.PostRepository;
import com.ll.commars.domain.community.reaction.dto.ReactionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostQueryService {

	private final PostRepository postRepository;

	@Transactional(readOnly = true)
	public PostResponse getPost(Long postId) {
		Post post = postRepository.findPostWithDetailsById(postId)
			.orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

		List<CommentResponse> comments = post.getComments().stream()
			.map(CommentResponse::from)
			.toList();

		List<PostHashTagResponse> postHashTags = post.getPostHashTags().stream()
			.map(PostHashTagResponse::from)
			.toList();

		List<ReactionResponse> reactions = post.getReactions().stream()
			.map(ReactionResponse::from)
			.toList();

		return PostResponse.of(post, comments, postHashTags, reactions);
	}
}
