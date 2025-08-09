package com.ll.commars.domain.community.post.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.community.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT p FROM Post p " +
		"LEFT JOIN FETCH p.member " +
		"LEFT JOIN FETCH p.comments " +
		"LEFT JOIN FETCH p.postHashTags " +
		"LEFT JOIN FETCH p.postLikes " +
		"WHERE p.id = :postId")
	Optional<Post> findPostWithDetailsById(@Param("postId") Long postId);

	@EntityGraph(attributePaths = {"user"})
	Optional<Post> findById(Long id);
}
