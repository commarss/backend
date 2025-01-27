package com.ll.commars.domain.home.board.controller;

import com.ll.commars.domain.home.board.entity.*;
import com.ll.commars.domain.home.board.repository.BoardRepository;
import com.ll.commars.domain.home.board.service.BoardService;
import com.ll.commars.domain.home.board.service.CommentService;
import com.ll.commars.domain.home.board.service.UserService;
import com.ll.commars.domain.home.loginfunction.controller.UserController;
import com.ll.commars.domain.home.loginfunction.dto.LoginDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BoardController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getPosts() {
        try {
            // 모든 게시글을 가져오기
            var posts = boardService.getAllBoards();
            Map<String, Object> response = new HashMap<>();
            response.put("posts", posts.stream().map(board -> {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("boardId", board.getBoardId());
                postMap.put("title", board.getTitle());

                // Board 객체에서 HashTag 리스트를 가져와서 tags만 추출
                List<String> tags = board.getHashTags().stream()
                        .map(HashTag::getTag)
                        .collect(Collectors.toList());
                postMap.put("tags", tags);
                return postMap;
            }).collect(Collectors.toList()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("게시글 조회 중 오류 발생: " + e.getMessage());
        }
    }



    // 게시글 작성
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> request, HttpSession session) {

        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }

            String title = (String) request.get("title");
            String content = (String) request.get("content");
            List<String> tags = (List<String>) request.get("tags");

            logger.info("게시글 작성 - 사용자 ID: {}", user.getUserId());
            boardService.addBoard(user.getUserId(), title, content, tags);
            return ResponseEntity.status(201).body("게시글이 등록되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("게시글 등록 중 오류 발생: " + e.getMessage());
        }
    }


    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable("postId") int postId) {
        System.out.println("postId " + postId);
        try {
            Board board = boardService.getBoard(postId);
            List<Comment> comments = commentService.getCommentsByBoardId(postId);

            Map<String, Object> response = new HashMap<>();
            Map<String, Object> boardMap = new HashMap<>();

            // 필요한 정보만 반환하도록 수정
            boardMap.put("boardId", board.getBoardId());
            boardMap.put("title", board.getTitle());
            boardMap.put("content", board.getContent());
            boardMap.put("regdate", board.getRegdate());
            boardMap.put("viewCnt", board.getViewCnt());
            boardMap.put("likes", board.getLikes());  // 좋아요 수 추가

            // User 객체에서 필요한 정보만 반환
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", board.getUser().getUserId());
            userMap.put("email", board.getUser().getEmail());
            userMap.put("name", board.getUser().getName());
            boardMap.put("user", userMap);

            // HashTags에서 tag만 추출하여 리스트로 반환
            List<String> hashTags = new ArrayList<>();
            for (HashTag tag : board.getHashTags()) {
                hashTags.add(tag.getTag());  // tag 값만 추가
            }
            boardMap.put("hashTags", hashTags);

            // board 객체에 대한 세부 정보 설정
            response.put("board", boardMap);
            response.put("comments", comments);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("게시글을 찾을 수 없습니다.");
        }
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable("postId") int postId,
            @RequestBody Map<String, Object> request,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user"); // LoginInfo가 아니라 User 객체를 가져옴
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String title = (String) request.get("title");
        String content = (String) request.get("content");
        List<String> tags = (List<String>) request.get("tags");

        try {
            boardService.updateBoard(postId, title, content, tags);
            return ResponseEntity.ok("게시글이 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 수정 중 오류 발생: " + e.getMessage());
        }
    }


    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") int postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            boardService.deleteBoard(postId);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }


    // 댓글 추가
    // 댓글 추가
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable("postId") int postId,
            @RequestBody Map<String, String> request,
            HttpSession session
    ) {
        // 세션에서 'user' 객체 가져오기 (LoginInfo가 아니라 User 객체일 경우)
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        String content = request.get("content");
        try {
            commentService.addComment(postId, user.getUserId(), content); // 로그인한 user의 ID 사용
            return ResponseEntity.ok(Map.of("message", "댓글이 추가되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "댓글 추가 중 오류 발생: " + e.getMessage()));
        }
    }

    // 좋아요 증가 API
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable("postId") int postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            int updatedLikes = boardService.incrementLikes(postId);
            return ResponseEntity.ok(Map.of("message", "좋아요가 추가되었습니다.", "likes", updatedLikes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "좋아요 추가 중 오류 발생", "error", e.getMessage()));
        }
    }



}