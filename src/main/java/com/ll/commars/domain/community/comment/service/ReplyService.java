package com.ll.commars.domain.community.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.comment.entity.Reply;
import com.ll.commars.domain.community.comment.repository.CommentRepository;
import com.ll.commars.domain.community.comment.repository.ReplyRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyRepository replyRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	public Reply addReply(Long commentId, Long userId, String content) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

		Reply reply = Reply.builder()
			.content(content)
			.user(user)
			.comment(comment)
			.build();
		return replyRepository.save(reply);
	}

	public void deleteReply(Long replyId, Long userId) {
		Reply reply = replyRepository.findById(replyId)
			.orElseThrow(() -> new RuntimeException("대댓글을 찾을 수 없습니다."));
		if (!reply.getUser().getId().equals(userId)) {
			throw new RuntimeException("삭제 권한이 없습니다.");
		}
		replyRepository.delete(reply);
	}

	@Transactional(readOnly = true)  // ✅ 세션 유지
	public List<ReplyDto> getRepliesByCommentId(Long commentId) {
		List<Reply> replies = replyRepository.findByCommentId(commentId);
		return replies.stream().map(ReplyDto::new).collect(Collectors.toList());
	}

}

