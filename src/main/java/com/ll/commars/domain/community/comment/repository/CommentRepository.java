package com.ll.commars.domain.community.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.community.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
