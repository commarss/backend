package com.ll.commars.domain.community.board.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.community.board.entity.BoardHashTag;
import com.ll.commars.domain.community.board.repository.BoardRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	@Transactional
	public Long addBoard(Long userId, String title, String content, List<BoardHashTag> boardHashTags, String imageUrl) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

		Board board = new Board(title, content, imageUrl, user, boardHashTags);

		board = boardRepository.save(board);
		return board.getId();
	}

	public List<Board> getAllBoards() {
		return boardRepository.findAll();
	}

	@Transactional
	public Board getBoard(Long postId) {
		Board board = boardRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

		board.incrementViews();
		return board;
	}

	@Transactional
	public void updateBoard(Long postId, String title, String content, List<BoardHashTag> tags) {
		Board board = boardRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

		board.updateBoard(title, content, tags);

		boardRepository.save(board);
	}

	@Transactional
	public void deleteBoard(Long boardId) {
		boardRepository.deleteById(boardId);
	}
}
