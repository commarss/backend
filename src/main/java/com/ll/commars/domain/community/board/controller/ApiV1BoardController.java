package com.ll.commars.domain.community.board.controller;

import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.community.comment.entity.Comment;

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

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 작성 ")
    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> request, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }

            String title = (String) request.get("title");
            String content = (String) request.get("content");
            List<String> tags = (List<String>) request.get("tags");

            String imageUrl = "wetwet";

            boardService.addBoard(user.getId(), title, content, tags, imageUrl);
            return ResponseEntity.status(201).body("게시글이 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 등록 중 오류 발생: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{postId}",consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 수정")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId,
                                        @RequestBody Map<String, Object> request,
                                        HttpSession session) {
        User user = (User) session.getAttribute("user");
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

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId, HttpSession session) {
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

    @PostMapping(value = "/{postId}/comments", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "댓글 추가")
    public ResponseEntity<?> addComment(@PathVariable("postId") Long postId,
                                        @RequestBody Map<String, String> request,
                                        HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String content = request.get("content");
        try {
            commentService.addComment(postId, user.getId(), content);
            return ResponseEntity.ok(Map.of("message", "댓글이 추가되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "댓글 추가 중 오류 발생: " + e.getMessage()));
        }
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
    public ResponseEntity<?> updateComment(@PathVariable Long postId,
                                           @PathVariable Long commentId,
                                           @RequestBody Map<String, String> request,
                                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String content = request.get("content");
        commentService.updateComment(commentId, user.getId(), content);
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId,
                                           @PathVariable Long commentId,
                                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

//    @PostMapping(value = "/{postId}/comments/{commentId}/replies", consumes = APPLICATION_JSON_VALUE)
//    @Operation(summary = "대댓글 추가")
//    public ResponseEntity<?> addReply(@PathVariable Long postId,
//                                      @PathVariable Long commentId,
//                                      @RequestBody Map<String, String> request,
//                                      HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//        }
//
//        String content = request.get("content");
//        commentService.addReply(postId, commentId, user.getId(), content);
//        return ResponseEntity.ok("대댓글이 추가되었습니다.");
//    }

    @PostMapping("/{postId}/reaction")
    @Operation(summary = "게시글 좋아요 ON/OFF")
    public ResponseEntity<?> toggleReaction(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        boolean isLiked = reactionService.toggleReaction(postId, user.getId());
        return ResponseEntity.ok(Map.of("liked", isLiked));
    }

    @GetMapping("/{postId}/reactions")
    @Operation(summary = "좋아요/싫어요 개수 조회")
    public ResponseEntity<?> getReactions(@PathVariable Long postId) {
        Map<String, Integer> reactions = reactionService.getReactions(postId);
        return ResponseEntity.ok(reactions);
    }

}
