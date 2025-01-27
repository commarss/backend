package com.ll.commars.domain.home.board.repository;

import com.ll.commars.domain.home.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    // 게시글 목록 페이징
    Page<Board> findAllByOrderByBoardIdDesc(Pageable pageable);

    // 제목으로 검색
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    // 제목으로 검색한 게시글 수
    long countByTitleContaining(String keyword);
}
