//package com.ll.commars.domain.restaurant.restaurantDoc.repository;
//
//import com.ll.commars.domain.restaurant.restaurantDoc.document.RestaurantDoc;
//import org.springframework.data.elasticsearch.annotations.Query;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface RestaurantDocRepository extends ElasticsearchRepository<RestaurantDoc, String> {
//    @Query("""
//            {
//                "bool": {
//                    "should": [
//                        {
//                            "match": {
//                                "name": "?0"
//                            }
//                        },
//                        {
//                            "match": {
//                                "details": "?0"
//                            }
//                        }
//                    ]
//                }
//            }
//    """)
//    List<RestaurantDoc> searchByKeyword(String keyword);
//
//    List<RestaurantDoc> findAllByOrderByAverageRateDesc();
//}
