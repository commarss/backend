package com.ll.commars.domain.community.post.dto;

import java.util.List;

import com.ll.commars.domain.community.comment.dto.CommentListResponse;
import com.ll.commars.domain.community.comment.dto.CommentResponse;
import com.ll.commars.domain.community.post.entity.Post;

public record PostDetailResponse(
	Long id,
	String title,
	String content,
	Integer views,
	String imageUrl,
	Integer likeCount,
	Integer dislikeCount,
	Long authorId,
	CommentListResponse commentListResponse,
	PostHashTagListResponse postHashTagListResponse,
	PostLikeListResponse postLikeListResponse
) {

	public static PostDetailResponse of(Post post, List<CommentResponse> comments, List<PostHashTagResponse> postHashTags, List<PostLikeResponse> reactions) {
		return new PostDetailResponse(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getViews(),
			post.getImageUrl(),
			post.getLikeCount(),
			post.getDislikeCount(),
			post.getUser().getId(),
			CommentListResponse.of(comments),
			PostHashTagListResponse.of(postHashTags),
			PostLikeListResponse.of(reactions)
		);
	}
}
