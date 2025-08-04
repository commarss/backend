package com.ll.commars.domain.community.board.controller;

import static org.springframework.http.MediaType.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.community.board.repository.BoardRepository;
import com.ll.commars.domain.community.board.service.BoardService;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final BoardRepository boardRepository;

    @GetMapping
    public ResponseEntity<?> getPosts() {
        try {
            List<Board> posts = boardService.getAllBoards();
            List<Map<String, Object>> postList = posts.stream().map(board -> {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("boardId", board.getId());
                postMap.put("title", board.getTitle());
                postMap.put("hashTags", board.getBoardHashTags());
                return postMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("posts", postList));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 조회 중 오류 발생: " + e.getMessage());
        }
    }

    private User getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(
            () -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable("postId") Long postId) {
        try {
            Board board = boardService.getBoard(postId);
            System.out.println("게시글 조회 성공: " + board.getTitle()); // 콘솔에 출력

            Map<String, Object> response = new HashMap<>();
            response.put("boardId", board.getId());
            response.put("title", board.getTitle());
            response.put("content", board.getContent());
            response.put("viewCnt", board.getViews());
            response.put("hashTags", board.getBoardHashTags());

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", board.getUser().getId());
            userMap.put("email", board.getUser().getEmail());
            userMap.put("name", board.getUser().getName());
            response.put("user", userMap);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("게시글 조회 실패! 오류 메시지: " + e.getMessage()); // 오류 출력
            e.printStackTrace(); // 전체 오류 로그 출력
            return ResponseEntity.status(404).body("게시글을 찾을 수 없습니다.");
        }
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> request,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String title = (String)request.get("title");
        String content = (String)request.get("content");
        List<String> tags = (List<String>)request.get("tags");
        String imageUrl = (String)request.get("imageUrl"); // ✅ imageUrl 가져오기
        boardService.addBoard(user.getId(), title, content, tags, imageUrl);
        return ResponseEntity.status(201).body("게시글이 등록되었습니다.");
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId,
        @RequestBody Map<String, Object> request,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String title = (String)request.get("title");
        String content = (String)request.get("content");
        List<String> tags = (List<String>)request.get("tags");
        boardService.updateBoard(postId, title, content, tags);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        boardService.deleteBoard(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @GetMapping("/count")
    public ResponseEntity<?> getUserPostCount(@RequestParam("email") String email) {
        int postCount = boardRepository.countPostsByEmail(email);
        return ResponseEntity.ok(Map.of("count", postCount));
    }
}