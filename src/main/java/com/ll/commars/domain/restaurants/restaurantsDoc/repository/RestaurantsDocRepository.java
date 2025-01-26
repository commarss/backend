package com.ll.commars.domain.restaurants.restaurantsDoc.repository;

import com.ll.commars.domain.restaurants.restaurantsDoc.document.RestaurantsDoc;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantsDocRepository extends ElasticsearchRepository<RestaurantsDoc, String> {
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
                            "details": "?0"
                        }
                    }
                ]
            }
        }
    """)
    List<RestaurantsDoc> searchByKeyword(String keyword);
}
