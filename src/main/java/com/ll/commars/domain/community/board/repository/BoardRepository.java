package com.ll.commars.domain.community.board.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.community.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	// 게시글 목록 페이징
	Page<Board> findAllByOrderByIdDesc(Pageable pageable);

	// 제목으로 검색
	Page<Board> findByTitleContaining(String keyword, Pageable pageable);

	// 제목으로 검색한 게시글 수
	long countByTitleContaining(String keyword);

	@Query("SELECT COUNT(b) FROM Board b WHERE b.user.email = :email")
	int countPostsByEmail(@Param("email") String email);

	@EntityGraph(attributePaths = {"user"})
		// ✅ Eager Fetch로 변경
	Optional<Board> findById(Long id);

}
