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
import com.ll.commars.domain.community.post.dto.PostDetailResponse;
import com.ll.commars.domain.community.post.dto.PostListResponse;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.repository.PostRepository;
import com.ll.commars.domain.community.post.service.PostCommandService;
import com.ll.commars.domain.community.post.service.PostQueryService;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
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

    @GetMapping("/{post-id}")
    public ResponseEntity<PostDetailResponse> getPost(
        @PathVariable("postId") Long postId
    ) {
        postCommandService.incrementViews(postId);
        PostDetailResponse response = postQueryService.getPost(postId);
        return ResponseEntity.ok(response);
    }

    // todo: paging 적용
    @GetMapping
    public ResponseEntity<PostListResponse> getPosts() {
        PostListResponse response = postQueryService.getPosts();
        return ResponseEntity.ok(response);
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