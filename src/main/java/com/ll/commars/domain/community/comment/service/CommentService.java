package com.ll.commars.domain.community.comment.service;


import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.community.board.repository.BoardRepository;
import com.ll.commars.domain.community.comment.repository.CommentRepository;
import com.ll.commars.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addComment(Long postId, Long userId, String content) {
        // 예시 구현
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid postId: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setUser(user);
        comment.setContent(content);

        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoard_Id(boardId);
    }
}
