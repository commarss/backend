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
        return commentRepository.findByBoard_IdWithUser(boardId);
    }

    public void truncate() {
        commentRepository.deleteAll();
    }




    //추가기능
    // ✅ 댓글 수정
    @Transactional
    public void updateComment(Long commentId, Long userId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        comment.setContent(content);
        commentRepository.save(comment);
    }

    // ✅ 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }



        commentRepository.delete(comment);
    }

    @Transactional
    public void addReply(Long postId, Long parentCommentId, Long userId, String content) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글 없음"));

        Comment reply = new Comment();
        reply.setBoard(parentComment.getBoard());
        reply.setUser(userRepository.findById(userId).orElseThrow());
        reply.setContent(content);
//        reply.setParentComment(parentComment);

        commentRepository.save(reply);
    }

}
/*
@Transactional
    public void addComment(Long postId, Long userId, String content) {
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

    // ✅ 댓글 수정
    @Transactional
    public void updateComment(Long commentId, Long userId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        comment.setContent(content);
        commentRepository.save(comment);
    }

    // ✅ 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    // ✅ 대댓글 추가
    @Transactional
    public void addReply(Long postId, Long parentCommentId, Long userId, String content) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + parentCommentId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        Comment reply = new Comment();
        reply.setBoard(parentComment.getBoard());
        reply.setUser(user);
        reply.setContent(content);
        reply.setParentComment(parentComment);

        commentRepository.save(reply);
    }

    // ✅ 특정 게시글의 댓글 조회
    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoard_IdWithUser(boardId);
    }
 */