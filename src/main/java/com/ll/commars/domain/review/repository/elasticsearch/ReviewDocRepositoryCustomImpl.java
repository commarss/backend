package com.ll.commars.domain.review.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ll.commars.domain.review.entity.ReviewDoc;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewDocRepositoryCustomImpl implements ReviewDocRepositoryCustom {

	private final ElasticsearchOperations elasticsearchOperations;

	@Override
	public List<ReviewDoc> searchByKeyword(String keyword) {
		// 키워드 검색 쿼리
		MultiMatchQuery textQuery = MultiMatchQuery.of(m -> m
			.query(keyword)
			.fields("title^2", "body")
			.operator(Operator.Or)
			.fuzziness("AUTO")
		);

		BoolQuery boolQuery = BoolQuery.of(b -> b
			.should(textQuery._toQuery())
		);

		Query query = NativeQuery.builder()
			.withQuery(q -> q
				.functionScore(fs -> fs
					.query(boolQuery._toQuery())
					.functions(f -> f
						.fieldValueFactor(fv -> fv
							.field("rate")
							.factor(1.5)
							.modifier(FieldValueFactorModifier.Sqrt)
							.missing(0.0)
						)
					)
					.boostMode(FunctionBoostMode.Multiply)
				)
			)
			.build();

		return elasticsearchOperations.search(query, ReviewDoc.class)
			.stream()
			.map(SearchHit::getContent)
			.toList();
	}
}
