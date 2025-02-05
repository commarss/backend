package com.ll.commars.domain.community.comment.repository;

import com.ll.commars.domain.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.board.id = :boardId")
    List<Comment> findByBoard_IdWithUser(@Param("boardId") Long boardId);


    // ✅ 특정 부모 댓글의 대댓글 조회
    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId ORDER BY c.id ASC")
    List<Comment> findByParentCommentId(Long parentCommentId);
}
