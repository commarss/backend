package com.ll.commars.domain.review.repository.elasticsearch;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.ll.commars.domain.review.entity.ReviewDoc;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewDocRepositoryCustomImpl implements ReviewDocRepositoryCustom {

	private final ElasticsearchOperations elasticsearchOperations;

	@Override
	public List<ReviewDoc> searchByKeyword(String keyword) {
		if (!StringUtils.hasText(keyword)) {
			return Collections.emptyList();
		}

		Query query = NativeQuery.builder()
			.withQuery(q -> q
				.multiMatch(mm -> mm
					.query(keyword)
					.fields("title", "body")
					.operator(Operator.And)
				)
			)
			.withSort(s -> s
				.field(f -> f
					.field("rate")
					.order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)
				)
			)
			.build();

		return elasticsearchOperations.search(query, ReviewDoc.class)
			.stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toList());
	}
}
