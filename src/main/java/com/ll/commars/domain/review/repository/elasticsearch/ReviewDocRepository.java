package com.ll.commars.domain.review.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ll.commars.domain.review.entity.ReviewDoc;

public interface ReviewDocRepository extends ElasticsearchRepository<ReviewDoc, String> {

	@Query("""
		    {
		        "function_score": {
		            "query": {
		                "bool": {
		                    "should": [
		                        {
		                            "multi_match": {
		                                "query": "?0",
		                                "fields": ["title^2", "body"],
		                                "operator": "or",
		                                "fuzziness": "AUTO"
		                            }
		                        }
		                    ]
		                }
		            },
		            "functions": [
		                {
		                    "field_value_factor": {
		                        "field": "rate",
		                        "factor": 1.5,
		                        "modifier": "sqrt",
		                        "missing": 0
		                    }
		                }
		            ],
		            "boost_mode": "multiply"
		        }
		    }
		""")
	List<ReviewDoc> searchByKeyword(String keyword);
}
