package com.ll.commars.domain.community.board.controller;

import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.community.comment.dto.ReplyDto;
import com.ll.commars.domain.community.comment.entity.Comment;

import com.ll.commars.domain.community.comment.entity.Reply;
import com.ll.commars.domain.community.comment.service.ReplyService;
import com.ll.commars.domain.community.reaction.service.ReactionService;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.community.board.service.BoardService;
import com.ll.commars.domain.community.comment.service.CommentService;
import com.ll.commars.domain.user.user.service.UserService;
import com.ll.commars.domain.community.board.repository.BoardRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping(value = "/api/posts", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1BoardController", description = "게시글 관련 API")
public class ApiV1BoardController {
    private static final Logger logger = LoggerFactory.getLogger(ApiV1BoardController.class);
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;
    private final BoardRepository boardRepository;
    public final ReactionService reactionService;
    private final ReplyService replyService;

    @GetMapping
    @Operation(summary = "모든 게시글 조회")
    public ResponseEntity<?> getPosts() {
        try {
            List<Board> posts = boardService.getAllBoards();
            List<Map<String, Object>> postList = posts.stream().map(board -> {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("boardId", board.getId());
                postMap.put("title", board.getTitle());
                postMap.put("hashTags", board.getHashTags());
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
    @Operation(summary = "특정 게시글 상세 조회")
    public ResponseEntity<?> getPostDetail(@PathVariable("postId") Long postId) {
        try {
            Board board = boardService.getBoard(postId);
            System.out.println("게시글 조회 성공: " + board.getTitle()); // 콘솔에 출력

            Map<String, Object> response = new HashMap<>();
            response.put("boardId", board.getId());
            response.put("title", board.getTitle());
            response.put("content", board.getContent());
            response.put("viewCnt", board.getViews());
            response.put("hashTags", board.getHashTags());

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
    @PutMapping("/{postId}/increment-views")
    @Operation(summary = "게시글 조회수 증가")
    public ResponseEntity<?> incrementPostViews(@PathVariable("postId") Long postId) {
        try {
            boardService.incrementViews(postId);
            return ResponseEntity.ok("조회수 증가 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("조회수 증가 실패");
        }
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 작성")
    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> request,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        List<String> tags = (List<String>) request.get("tags");
        String imageUrl = (String) request.get("imageUrl"); // ✅ imageUrl 가져오기
        boardService.addBoard(user.getId(), title, content, tags, imageUrl);
        return ResponseEntity.status(201).body("게시글이 등록되었습니다.");
    }

    @PutMapping(value = "/{postId}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 수정")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId,
                                        @RequestBody Map<String, Object> request,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        List<String> tags = (List<String>) request.get("tags");
        boardService.updateBoard(postId, title, content, tags);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        boardService.deleteBoard(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @PostMapping(value = "/{postId}/comments", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "댓글 추가")
    public ResponseEntity<?> addComment(@PathVariable("postId") Long postId,
                                        @RequestBody Map<String, String> request,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String content = request.get("content");
        commentService.addComment(postId, user.getId(), content);
        return ResponseEntity.ok("댓글이 추가되었습니다.");
    }

    @GetMapping("/count")
    @Operation(summary = "특정 사용자의 게시글 개수 조회")
    public ResponseEntity<?> getUserPostCount(@RequestParam("email") String email) {
        int postCount = boardRepository.countPostsByEmail(email);
        return ResponseEntity.ok(Map.of("count", postCount));
    }

    @GetMapping("/{postId}/comments")
    @Operation(summary = "특정 게시글의 댓글 조회")
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

    @PutMapping(value = "/{postId}/comments/{commentId}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "댓글 수정")
    public ResponseEntity<?> updateComment(@PathVariable("postId") Long postId,
                                           @PathVariable("commentId") Long commentId,
                                           @RequestBody Map<String, String> request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String content = request.get("content");
        commentService.updateComment(commentId, user.getId(), content);
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<?> deleteComment(@PathVariable("postId") Long postId,
                                           @PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @PostMapping(value = "/{postId}/comments/{commentId}/replies", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "대댓글 추가")
    public ResponseEntity<?> addReply(@PathVariable("commentId") Long commentId,
                                      @RequestBody Map<String, String> request,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        String content = request.get("content");
        Reply reply = replyService.addReply(commentId, user.getId(), content);

        return ResponseEntity.ok(new ReplyDto(reply)); // ✅ DTO로 반환
    }



    @PostMapping("/{postId}/reaction")
    @Operation(summary = "게시글 좋아요 ON/OFF")
    public ResponseEntity<?> toggleReaction(@PathVariable("postId") Long postId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // UserDetails에서 userId 추출 (예제: userDetails가 CustomUserDetails라면 getId() 사용)
        Long userId = Long.parseLong(userDetails.getUsername()); // 필요 시 커스텀 UserDetails에 맞게 수정

        boolean isLiked = reactionService.toggleReaction(postId, userId);
        return ResponseEntity.ok(Map.of("liked", isLiked));
    }

    @GetMapping("/{postId}/reactions")
    @Operation(summary = "좋아요/싫어요 개수 조회")
    public ResponseEntity<?> getReactions(@PathVariable("postId") Long postId) {
        Map<String, Integer> reactions = reactionService.getReactions(postId);
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/{postId}/comments/{commentId}/replies")
    @Operation(summary = "대댓글 조회")
    public ResponseEntity<List<ReplyDto>> getReplies(@PathVariable("commentId") Long commentId) {
        List<ReplyDto> replies = replyService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }


}