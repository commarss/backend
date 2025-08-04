package com.ll.commars.domain.community.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.community.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.board.id = :boardId")
	List<Comment> findByBoard_IdWithUser(@Param("boardId") Long boardId);
}
