package com.ll.commars.domain.community.post.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.community.post.entity.PostHashTag;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

	List<PostHashTag> findAllByPostId(Long postId);
}
