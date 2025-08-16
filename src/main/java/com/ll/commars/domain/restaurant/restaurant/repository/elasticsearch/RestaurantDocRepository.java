package com.ll.commars.domain.restaurant.restaurant.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;

public interface RestaurantDocRepository extends ElasticsearchRepository<RestaurantDoc, String> {

	// name에 2배 가중치
	@Query("""
		    {
		        "function_score": {
		            "query": {
		                "bool": {
		                    "must": [
		                        {
		                            "multi_match": {
		                                "query": "?0",
		                                "fields": ["name^2", "details"],
		                                "operator": "or",
		                                "fuzziness": "AUTO"
		                            }
		                        }
		                    ],
		                    "filter": {
		                        "geo_distance": {
		                            "distance": ?3,
		                            "location": {
		                                "lat": ?1,
		                                "lon": ?2
		                            }
		                        }
		                    }
		                }
		            },
		            "functions": [
		                {
		                    "field_value_factor": {
		                        "field": "average_rate",
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
	List<RestaurantDoc> searchByKeywordAndLocation(String keyword, double lat, double lon, double distance);

	List<RestaurantDoc> findTop5ByOrderByAverageRateDesc();

	@Query("""
    {
        "query": {
            "bool": {
                "filter": {
                    "geo_distance": {
                        "distance": ?2,
                        "location": {
                            "lat": ?0,
                            "lon": ?1
                        }
                    }
                }
            }
        },
        "sort": [
            {
                "_geo_distance": {
                    "location": {
                        "lat": ?0,
                        "lon": ?1
                    },
                    "order": "asc",
                    "unit": "m"
                }
            }
        ]
    }
""")
	List<RestaurantDoc> findNearbyRestaurantsSortedByDistance(Double lat, Double lon, Double distance);
}
