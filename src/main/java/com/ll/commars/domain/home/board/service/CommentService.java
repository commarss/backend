package com.ll.commars.domain.home.board.service;


import com.ll.commars.domain.home.board.entity.Board;
import com.ll.commars.domain.home.board.entity.Comment;
import com.ll.commars.domain.home.board.entity.User;
import com.ll.commars.domain.home.board.repository.BoardRepository;
import com.ll.commars.domain.home.board.repository.CommentRepository;
import com.ll.commars.domain.home.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addComment(int postId, int userId, String content) {
        // 예시 구현
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid postId: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedDate(LocalDateTime.now());

        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByBoardId(int boardId) {
        return commentRepository.findByBoard_BoardId(boardId);
    }
}
