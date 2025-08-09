package com.ll.commars.domain.community.post.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.community.post.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

	List<PostLike> findAllByPostId(Long postId);
}
