package com.ll.commars.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAnalysisDTO {

	private String name;  // 리뷰 작성자 이름
	private String body;  // 리뷰 내용
	private int rate;     // 평점
}
