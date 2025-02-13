package com.ll.commars.domain.restaurant.restaurantDoc.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ll.commars.domain.restaurant.restaurantDoc.document.RestaurantDoc;
import com.ll.commars.domain.restaurant.restaurantDoc.repository.RestaurantDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantDocService {
   private final RestaurantDocRepository restaurantDocRepository;
   private final ElasticsearchClient elasticsearchClient;

   public RestaurantDoc write(String name, String details, Double averageRate, Double lat, Double lng) {
       RestaurantDoc restaurantDoc = RestaurantDoc.builder()
               .name(name)
               .details(details)
               .averageRate(averageRate)
               .location(lat + "," + lng)
               .build();

       return restaurantDocRepository.save(restaurantDoc);
   }

   public void truncate() {
       restaurantDocRepository.deleteAll();
   }

    public List<RestaurantDoc> searchByKeyword(String keyword, double userLat, double userLng, String distance) throws IOException {
        Query matchNameQuery = MatchQuery.of(m -> m
                .field("name")
                .query(keyword)
                .fuzziness("AUTO")
        )._toQuery();

        Query matchDetailsQuery = MatchQuery.of(m -> m
                .field("details")
                .query(keyword)
                .fuzziness("AUTO")
        )._toQuery();

        // ✅ 사용자 위치 기준 반경 검색 (Geo Distance)
        Query geoDistanceQuery = GeoDistanceQuery.of(g -> g
                .field("location")  // ES의 GeoPoint 필드
                .distance(distance) // 검색 반경 (예: "50km")
                .location(GeoLocation.of(l -> l.latlon(LatLonGeoLocation.of(ll -> ll.lat(userLat).lon(userLng))))) // ✅ 수정된 부분
        )._toQuery();

        // 키워드 + 거리 필터 조합
        Query boolQuery = BoolQuery.of(b -> b
                .should(matchNameQuery)
                .should(matchDetailsQuery)
                .filter(geoDistanceQuery) // ✅ 거리 필터 추가
        )._toQuery();

        // Function Score Query (평점 높은 곳 우선)
        FunctionScoreQuery functionScoreQuery = FunctionScoreQuery.of(f -> f
                .query(boolQuery)
                .functions(FunctionScore.of(fs -> fs
                        .fieldValueFactor(FieldValueFactorScoreFunction.of(fv -> fv
                                .field("average_rate")
                                .factor(1.5)
                                .modifier(FieldValueFactorModifier.Sqrt)
                        ))
                ))
        );

        // 검색 요청
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("es_restaurants")
                .query(functionScoreQuery._toQuery())
        );

        // 검색 실행
        SearchResponse<RestaurantDoc> response = elasticsearchClient.search(searchRequest, RestaurantDoc.class);

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .collect(Collectors.toList());
    }

    public List<RestaurantDoc> showSortByRate() {
       return restaurantDocRepository.findAllByOrderByAverageRateDesc();
   }

    public List<RestaurantDoc> findNearbyRestaurants(Double lat, Double lng, Double distance) {
        return restaurantDocRepository.findByLocationNear(distance, lat, lng);
    }


}
