package com.ll.commars.domain.community.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.community.reaction.entity.Reaction;
import com.ll.commars.domain.user.user.entity.User;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

	// 특정 유저가 특정 게시글에 반응(좋아요/싫어요)을 남겼는지 확인
	Reaction findByBoardAndUser(Board board, User user);

	// 특정 게시글의 좋아요 개수 조회
	int countByBoardIdAndLiked(Long boardId, boolean liked);
}
