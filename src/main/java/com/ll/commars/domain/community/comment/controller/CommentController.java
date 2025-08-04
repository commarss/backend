package com.ll.commars.domain.community.comment.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.comment.entity.Reply;
import com.ll.commars.domain.community.comment.service.CommentService;
import com.ll.commars.domain.community.comment.service.ReplyService;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts/{postId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ReplyService replyService;
    private final UserService userService;

    private User getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(
            () -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@PathVariable("postId") Long postId,
        @RequestBody Map<String, String> request,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String content = request.get("content");
        commentService.addComment(postId, user.getId(), content);
        return ResponseEntity.ok("댓글이 추가되었습니다.");
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable("postId") Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByBoardId(postId);

            List<Map<String, Object>> commentList = comments.stream().map(comment -> {
                Map<String, Object> commentMap = new HashMap<>();
                commentMap.put("commentId", comment.getId());
                commentMap.put("content", comment.getContent());

                if (comment.getUser() != null) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("userId", comment.getUser().getId());
                    userMap.put("email", comment.getUser().getEmail());
                    userMap.put("name", comment.getUser().getName());
                    commentMap.put("user", userMap);
                } else {
                    commentMap.put("user", null);
                }

                return commentMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("postId", postId, "comments", commentList));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("댓글 조회 중 오류 발생: " + e.getMessage());
        }
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId,
        @RequestBody Map<String, String> request,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String content = request.get("content");
        commentService.updateComment(commentId, user.getId(), content);
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<?> addReply(@PathVariable("commentId") Long commentId,
        @RequestBody Map<String, String> request,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String content = request.get("content");
        Reply reply = replyService.addReply(commentId, user.getId(), content);

        return ResponseEntity.ok(new ReplyDto(reply)); // ✅ DTO로 반환
    }

    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<List<ReplyDto>> getReplies(@PathVariable("commentId") Long commentId) {
        List<ReplyDto> replies = replyService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }
}
