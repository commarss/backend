package com.ll.commars.domain.community.post.controller;

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

import com.ll.commars.domain.community.post.dto.PostCreateRequest;
import com.ll.commars.domain.community.post.dto.PostCreateResponse;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.repository.PostRepository;
import com.ll.commars.domain.community.post.service.PostCommandService;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostCommandService postCommandService;
    private final UserService userService;
    private final PostRepository postRepository;

    @PostMapping
    public ResponseEntity<PostCreateResponse> createPost(
        @RequestBody PostCreateRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = getAuthenticatedUser(userDetails);
        PostCreateResponse response = postCommandService.createPost(user.getId(), request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getPosts() {
        try {
            List<Post> posts = postCommandService.getAllBoards();
            List<Map<String, Object>> postList = posts.stream().map(board -> {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("boardId", board.getId());
                postMap.put("title", board.getTitle());
                postMap.put("hashTags", board.getPostHashTags());
                return postMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("posts", postList));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 조회 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable("postId") Long postId) {
        try {
            Post post = postCommandService.getBoard(postId);
            System.out.println("게시글 조회 성공: " + post.getTitle()); // 콘솔에 출력

            Map<String, Object> response = new HashMap<>();
            response.put("boardId", post.getId());
            response.put("title", post.getTitle());
            response.put("content", post.getContent());
            response.put("viewCnt", post.getViews());
            response.put("hashTags", post.getPostHashTags());

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", post.getUser().getId());
            userMap.put("email", post.getUser().getEmail());
            userMap.put("name", post.getUser().getName());
            response.put("user", userMap);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("게시글 조회 실패! 오류 메시지: " + e.getMessage()); // 오류 출력
            e.printStackTrace(); // 전체 오류 로그 출력
            return ResponseEntity.status(404).body("게시글을 찾을 수 없습니다.");
        }
    }

    private User getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(
            () -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId,
        @RequestBody Map<String, Object> request,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String title = (String)request.get("title");
        String content = (String)request.get("content");
        List<String> tags = (List<String>)request.get("tags");
        postCommandService.updateBoard(postId, title, content, tags);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        postCommandService.deleteBoard(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @GetMapping("/count")
    public ResponseEntity<?> getUserPostCount(@RequestParam("email") String email) {
        int postCount = postRepository.countPostsByEmail(email);
        return ResponseEntity.ok(Map.of("count", postCount));
    }
}