package com.ll.commars.domain.review.reviewDoc.repository;

import com.ll.commars.domain.review.reviewDoc.document.ReviewDoc;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewDocRepository extends ElasticsearchRepository<ReviewDoc, String> {
    @Query("""
            {
                "bool": {
                    "should": [
                        {
                            "match": {
                                "name": "?0"
                            }
                        },
                        {
                            "match": {
                                "body": "?0"
                            }
                        }
                    ]
                }
            }
    """)
    List<ReviewDoc> searchByKeyword(String keyword);
}
