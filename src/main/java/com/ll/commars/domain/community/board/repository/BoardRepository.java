package com.ll.commars.domain.community.board.repository;

import com.ll.commars.domain.community.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    // 게시글 목록 페이징
    Page<Board> findAllByOrderByBoardIdDesc(Pageable pageable);

    // 제목으로 검색
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    // 제목으로 검색한 게시글 수
    long countByTitleContaining(String keyword);

    @Query("SELECT COUNT(b) FROM Board b WHERE b.user.email = :email")
    int countPostsByEmail(@Param("email") String email);
}
