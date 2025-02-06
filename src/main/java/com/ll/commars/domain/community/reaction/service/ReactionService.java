package com.ll.commars.domain.community.reaction.service;

import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.community.reaction.entity.Reaction;
import com.ll.commars.domain.community.reaction.repository.ReactionRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;

    // ✅ 좋아요 ON/OFF 기능
    @Transactional
    public boolean toggleReaction(Long postId, Long userId) {
        Board board = new Board();
        board.setId(postId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        Reaction reaction = reactionRepository.findByBoardAndUser(board, user);

        if (reaction == null) {
            // 좋아요 추가
            reaction = new Reaction();
            reaction.setBoard(board);
            reaction.setUser(user);
            reaction.setLiked(true);
            reactionRepository.save(reaction);
            return true;
        } else {
            // 좋아요 취소 (토글 기능)
            reactionRepository.delete(reaction);
            return false;
        }
    }

    // ✅ 좋아요/싫어요 개수 조회
    public Map<String, Integer> getReactions(Long postId) {
        int likes = reactionRepository.countByBoardIdAndLiked(postId, true);
        int dislikes = reactionRepository.countByBoardIdAndLiked(postId, false);
        return Map.of("likes", likes, "dislikes", dislikes);
    }

    public void truncate(){
        reactionRepository.deleteAll();
    }
}
