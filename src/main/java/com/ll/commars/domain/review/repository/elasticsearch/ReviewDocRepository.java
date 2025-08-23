package com.ll.commars.domain.review.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ll.commars.domain.review.entity.ReviewDoc;

public interface ReviewDocRepository extends ElasticsearchRepository<ReviewDoc, String>, ReviewDocRepositoryCustom {

}
