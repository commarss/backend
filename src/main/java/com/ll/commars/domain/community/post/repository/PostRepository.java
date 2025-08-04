package com.ll.commars.domain.community.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.community.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {


	long countByTitleContaining(String keyword);

	@Query("SELECT COUNT(b) FROM Post b WHERE b.user.email = :email")
	int countPostsByEmail(@Param("email") String email);

	@EntityGraph(attributePaths = {"user"})
	Optional<Post> findById(Long id);
}
