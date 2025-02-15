package com.ll.commars.domain.restaurant.restaurantDoc.repository;

import com.ll.commars.domain.restaurant.restaurantDoc.document.RestaurantDoc;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantDocRepository extends ElasticsearchRepository<RestaurantDoc, String> {
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
            },
            "size": 5
        }
""")

   List<RestaurantDoc> searchByKeyword(String keyword);

   List<RestaurantDoc> findTop5ByOrderByAverageRateDesc();
   @Query("""
        {
            "bool": {
                "must": {
                    "geo_distance": {
                        "distance": "?0km",
                        "location": {
                            "lat": ?1,
                            "lon": ?2
                        }
                    }
                }
            },
            "size": 10
        }
""")

   List<RestaurantDoc> findByLocationNear(Double distance, Double lat, Double lng);
}
