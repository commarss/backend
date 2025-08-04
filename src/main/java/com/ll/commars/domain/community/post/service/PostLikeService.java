package com.ll.commars.domain.community.post.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.community.post.entity.PostLike;
import com.ll.commars.domain.community.post.repository.PostLikeRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;
	private final UserRepository userRepository;

	// ✅ 좋아요 ON/OFF 기능
	@Transactional
	public boolean toggleReaction(Long postId, Long userId) {
		Post post = new Post();
		post.setId(postId);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

		PostLike postLike = postLikeRepository.findByBoardAndUser(post, user);

		if (postLike == null) {
			// 좋아요 추가
			postLike = new PostLike();
			postLike.setPost(post);
			postLike.setUser(user);
			postLike.setLiked(true);
			postLikeRepository.save(postLike);
			return true;
		} else {
			// 좋아요 취소 (토글 기능)
			postLikeRepository.delete(postLike);
			return false;
		}
	}

	// ✅ 좋아요/싫어요 개수 조회
	public Map<String, Integer> getReactions(Long postId) {
		int likes = postLikeRepository.countByBoardIdAndLiked(postId, true);
		int dislikes = postLikeRepository.countByBoardIdAndLiked(postId, false);
		return Map.of("likes", likes, "dislikes", dislikes);
	}

	public void truncate() {
		postLikeRepository.deleteAll();
	}
}
