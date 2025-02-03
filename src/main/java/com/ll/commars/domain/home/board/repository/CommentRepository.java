package com.ll.commars.domain.home.board.repository;

import com.ll.commars.domain.home.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard_BoardId(int boardId);
}
