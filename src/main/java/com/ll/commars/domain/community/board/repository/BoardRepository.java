package com.ll.commars.domain.community.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.community.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {


	long countByTitleContaining(String keyword);

	@Query("SELECT COUNT(b) FROM Board b WHERE b.user.email = :email")
	int countPostsByEmail(@Param("email") String email);

	@EntityGraph(attributePaths = {"user"})
	Optional<Board> findById(Long id);
}
