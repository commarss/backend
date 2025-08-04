package com.ll.commars.domain.community.reaction.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.community.reaction.service.ReactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts/{postId}/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<?> toggleReaction(@PathVariable("postId") Long postId,
        @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Long userId = Long.parseLong(userDetails.getUsername());

        boolean isLiked = reactionService.toggleReaction(postId, userId);
        return ResponseEntity.ok(Map.of("liked", isLiked));
    }

    @GetMapping
    public ResponseEntity<?> getReactions(@PathVariable("postId") Long postId) {
        Map<String, Integer> reactions = reactionService.getReactions(postId);
        return ResponseEntity.ok(reactions);
    }
}
