package com.ll.commars.domain.home.board.service;

import com.ll.commars.domain.home.board.entity.Board;
import com.ll.commars.domain.home.board.entity.HashTag;
import com.ll.commars.domain.home.board.entity.User;
import com.ll.commars.domain.home.board.repository.BoardRepository;
import com.ll.commars.domain.home.board.repository.CommentRepository;
import com.ll.commars.domain.home.board.repository.HashTagRepository;
import com.ll.commars.domain.home.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final HashTagRepository hashTagRepository;
    private final UserRepository userRepository;

    // 게시글 추가
    public void addBoard(int userId, String title, String content, List<String> tags) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Board board = new Board();
        board.setUser(user);
        board.setTitle(title);
        board.setContent(content);
        board.setRegdate(LocalDateTime.now());
        board.setViewCnt(0);
        boardRepository.save(board);

        // 해시태그 저장
        List<HashTag> hashTags = tags.stream()
                .map(tag -> {
                    HashTag hashTag = new HashTag();
                    hashTag.setTag(tag);
                    hashTag.setBoard(board);
                    return hashTag;
                }).collect(Collectors.toList());
        hashTagRepository.saveAll(hashTags);
        board.setHashTags(hashTags);  // Board 객체에 태그 추가
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();  // 게시글을 모두 가져옴
    }


    // 게시글 상세 조회 (조회수 증가 포함)
    @Transactional
    public Board getBoard(int boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        // 조회수 증가
        board.setViewCnt(board.getViewCnt() + 1);
        boardRepository.save(board);

        // 태그 목록 변환
        List<String> tags = board.getHashTags().stream()
                .map(HashTag::getTag)
                .collect(Collectors.toList());
        board.setTags(tags);

        return board;
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(int postId, String title, String content, List<String> tags) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        board.setTitle(title);
        board.setContent(content);

        // 해시태그 업데이트
        List<HashTag> newHashTags = tags.stream()
                .map(tag -> {
                    HashTag hashTag = new HashTag();
                    hashTag.setTag(tag);
                    hashTag.setBoard(board);
                    return hashTag;
                }).collect(Collectors.toList());
        hashTagRepository.deleteByBoard(board);  // 기존 태그 삭제
        hashTagRepository.saveAll(newHashTags);  // 새 태그 저장

        boardRepository.save(board);
    }

    // 게시글 삭제
    public void deleteBoard(int boardId) {
        boardRepository.deleteById(boardId);
    }

    // 게시글 총 개수 조회
    public int getTotalCount(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return (int) boardRepository.count();
        } else {
            return (int) boardRepository.countByTitleContaining(keyword);
        }
    }

    // 좋아요 증가
    public int incrementLikes(int postId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
        board.increaseLikes();
        boardRepository.save(board);
        return board.getLikes();  // 업데이트된 좋아요 수 반환
    }


}