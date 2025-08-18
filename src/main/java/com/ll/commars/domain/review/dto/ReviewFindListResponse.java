package com.ll.commars.domain.review.dto;

import java.util.List;

public record ReviewFindListResponse(
	List<ReviewFindResponse> reviews
) {
}
