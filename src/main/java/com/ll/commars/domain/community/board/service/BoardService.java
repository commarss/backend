package com.ll.commars.domain.community.board.service;

import com.ll.commars.domain.community.board.entity.Board;

import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.comment.repository.CommentRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.community.board.repository.BoardRepository;

import com.ll.commars.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

 // Lombok 어노테이션
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@Slf4j // Lombok 어노테이션
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // 게시글 추가
    @Transactional
    public Long addBoard(Long userId, String title, String content, List<String> tags, String imageUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // ✅ List<String> → String 변환 (","로 구분)
        String tagsString = String.join(",", tags);

        // ✅ 변환된 String을 저장
        Board board = Board.builder()
                .user(user)
                .title(title)
                .content(content)
                .views(0)
                .hashTags(tagsString)  // 🚀 String 값으로 전달
                .dislikeCount(0)  // 🚀 기본값 0 추가
                .likeCount(0)     // 👍 같이 기본값 추가하는 것이 좋음
                .build();

        board = boardRepository.save(board);
        boardRepository.flush();  // 🔥 즉시 DB에 반영 (flush)
        return board.getId();
    }


    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // 게시글 상세 조회 (조회수 증가 포함)
    // ✅ 조회수 증가 로직 제거
    public Board getBoard(Long postId) {
        return boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }
    /*@Transactional
    public Board getBoard(Long postId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        // 조회수 증가
        board.setViews(board.getViews() + 1);
        boardRepository.save(board);
        boardRepository.flush();  // 🔥 즉시 DB에 반영

        return board;
    }*/

    // 게시글 수정
    @Transactional
    public void updateBoard(Long postId, String title, String content, List<String> tags) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        board.setTitle(title);
        board.setContent(content);
        board.setHashTags(tags);

        boardRepository.save(board);
    }

    // 게시글 삭제
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    // 게시글 총 개수 조회
    // ✅ 조회수 증가 메서드 분리
    @Transactional
    public void incrementViews(Long postId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        board.setViews(board.getViews() + 1);
        boardRepository.save(board);
    }

    public int getTotalCount(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return (int) boardRepository.count();
        } else {
            return (int) boardRepository.countByTitleContaining(keyword);
        }
    }

    @Transactional
    public void truncate() {

        commentRepository.deleteAll();
        boardRepository.deleteAll();
    }
}

/*
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    // 게시글 추가
    @Transactional
    public Long addBoard(Long userId, String title, String content, List<String> tags) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // ✅ HashTag를 Board 내부에 저장
        Board board = Board.builder()
                .user(user)
                .title(title)
                .content(content)
                .views(0)
                .hashTags(tags)  // 리스트 그대로 전달 가능
                .build();

        board = boardRepository.save(board);

        return board.getId();
    }







    public List<Board> getAllBoards() {
        return boardRepository.findAll();  // 게시글을 모두 가져옴
    }


    // 게시글 상세 조회 (조회수 증가 포함)
    @Transactional
    public Board getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        // 조회수 증가
        board.setViews(board.getViews() + 1);
        boardRepository.save(board);

        // 태그 목록 변환
//        List<String> tags = board.getHashTags().stream()
//                .map(HashTag::getTag)
//                .collect(Collectors.toList());
//        board.setTags(tags);

        return board;
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long postId, String title, String content, List<String> tags) {
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
    public void deleteBoard(Long boardId) {
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

    public void truncate() {
        boardRepository.deleteAll();
    }

    // 좋아요 증가
//    public int incrementLikes(int postId) {
//        Board board = boardRepository.findById(postId)
//                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
//        board.increaseLikes();
//        boardRepository.save(board);
//        return board.getLikes();  // 업데이트된 좋아요 수 반환
//    }
}

 */