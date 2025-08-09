package com.ll.commars.domain.review.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class ReviewDto {

	// 리뷰 정보
	@Getter
	@Builder
	public static class ReviewInfo {

		private Long id;
		private Long userId;
		private Long restaurantId;
		private String name;
		private String body;
		private Integer rate;
	}

	// 리뷰 작성 및 수정 시 요청
	@Getter
	@Builder
	public static class ReviewWriteRequest {

		private Long userId;
		private String reviewName;
		private String body;
		private Integer rate;
	}

	// 리뷰 작성 및 수정 시 응답
	@Getter
	@Builder
	public static class ReviewWriteResponse {

		private String userName;
		private String restaurantName;
		private String reviewName;
		private String body;
		private Integer rate;
	}

	// 모든 리뷰 조회 시 응답
	@Getter
	@Builder
	public static class ShowAllReviewsResponse {

		List<ReviewInfo> reviews;
	}
}
