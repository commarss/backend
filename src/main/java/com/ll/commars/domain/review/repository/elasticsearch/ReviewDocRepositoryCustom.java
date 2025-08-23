package com.ll.commars.domain.review.repository.elasticsearch;

import java.util.List;

import com.ll.commars.domain.review.entity.ReviewDoc;

public interface ReviewDocRepositoryCustom {
	List<ReviewDoc> searchByKeyword(String keyword);
}
