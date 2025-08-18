package com.ll.commars.domain.review.dto;

import java.util.List;

import com.ll.commars.domain.review.entity.ReviewDoc;

public record ReviewSearchResponse(
	List<ReviewSearchInfo> reviews,
	int totalCount
) {

	public static ReviewSearchResponse from(List<ReviewDoc> reviewDocs) {
		List<ReviewSearchInfo> reviewInfos = reviewDocs.stream()
			.map(ReviewSearchInfo::from)
			.toList();

		return new ReviewSearchResponse(reviewInfos, reviewInfos.size());
	}

	public record ReviewSearchInfo(
		Long memberId,
		String title,
		String content,
		Integer rate
	) {

		public static ReviewSearchInfo from(ReviewDoc reviewDoc) {
			return new ReviewSearchInfo(
				reviewDoc.getMemberId(),
				reviewDoc.getTitle(),
				reviewDoc.getBody(),
				reviewDoc.getRate()
			);
		}
	}
}
