package com.ll.commars.domain.community.post.dto;

import lombok.Builder;
import lombok.Getter;

public class BoardDto {

	// 게시글 기본 정보
	@Getter
	@Builder
	public static class BoardBasicInfo {

		private Long id;
		private String title;
		private String content;
		private Integer views;
		private String imageUrl;
		private Integer likeCount;
		private Integer dislikeCount;
	}
}
