package com.ll.commars.domain.reviews.reviewsDoc.repository;

import com.ll.commars.domain.reviews.reviewsDoc.document.ReviewsDoc;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsDocRepository extends ElasticsearchRepository<ReviewsDoc, String> {
    @Query("""
        {
            "bool": {
                "should": [
                    {
                        "match": {
                            "content": "?0"
                        }
                    }
                ]
            }
        }
    """)
    List<ReviewsDoc> searchByKeyword(String keyword);
}
