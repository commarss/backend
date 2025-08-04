package com.ll.commars.domain.reviewerRank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewerRank {

	private Long userId;
	private String name;   // 유저 이름
	private Long reviewCount;  // 작성한 리뷰 개수
}
