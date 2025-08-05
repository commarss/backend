package com.ll.commars.domain.community.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.comment.dto.CommentCreateRequest;
import com.ll.commars.domain.community.comment.dto.CommentCreateResponse;
import com.ll.commars.domain.community.comment.dto.CommentUpdateRequest;
import com.ll.commars.domain.community.comment.dto.CommentUpdateResponse;
import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.comment.repository.CommentRepository;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.repository.PostRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public CommentCreateResponse createComment(Long userId, CommentCreateRequest request) {

		Post post = postRepository.findById(request.id())
			.orElseThrow(() -> new IllegalArgumentException("Invalid postId: " + request.id()));

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

		Comment comment = new Comment(request.content(), user, post);

		return CommentCreateResponse.from(
			commentRepository.save(comment)
		);
	}

	@Transactional
	public CommentUpdateResponse updateComment(Long userId, Long commentId, CommentUpdateRequest request) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

		if (!comment.getUser().getId().equals(userId)) {
			throw new IllegalArgumentException("수정 권한이 없습니다.");
		}

		comment.updateContent(request.content());

		return CommentUpdateResponse.from(
			commentRepository.save(comment)
		);
	}

	@Transactional
	public void deleteComment(Long commentId, Long userId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

		if (!comment.getUser().getId().equals(userId)) {
			throw new IllegalArgumentException("삭제 권한이 없습니다.");
		}

		commentRepository.delete(comment);
	}
}
