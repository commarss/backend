package com.ll.commars.domain.community.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.community.comment.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

	List<Reply> findByCommentId(Long commentId);
}

