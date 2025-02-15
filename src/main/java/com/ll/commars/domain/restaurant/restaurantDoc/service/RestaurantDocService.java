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
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantDocService {
   private final RestaurantDocRepository restaurantDocRepository;
   private final ElasticsearchClient elasticsearchClient;

    public RestaurantDoc write(String name, String details, Double averageRate, Double lat, Double lon) {
        RestaurantDoc restaurantDoc = RestaurantDoc.builder()
                .name(name)
                .details(details)
                .averageRate(averageRate)
                .location(lat + "," + lon)
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
                .boost(2.0f)
        )._toQuery();

        Query matchDetailsQuery = MatchQuery.of(m -> m
                .field("details")
                .query(keyword)
                .fuzziness("AUTO")
                .boost(1.0f)
        )._toQuery();

        // ✅ 사용자 위치 기준 반경 검색 (Geo Distance)
        Query geoDistanceQuery = GeoDistanceQuery.of(g -> g
                .field("location")
                .distance(distance)
                .location(GeoLocation.of(l -> l.latlon(LatLonGeoLocation.of(ll -> ll.lat(userLat).lon(userLng)))))
                .boost(0.5f)
        )._toQuery();

        // 키워드 + 거리 필터 조합
        Query boolQuery = BoolQuery.of(b -> b
                .should(matchNameQuery)
                .should(matchDetailsQuery)
                .filter(geoDistanceQuery)
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
        return restaurantDocRepository.findTop5ByOrderByAverageRateDesc();
    }

    public List<RestaurantDoc> findNearbyRestaurants(Double lat, Double lon, Double distance) {
        try {
            // GeoDistance 쿼리 생성
            Query geoDistanceQuery = GeoDistanceQuery.of(g -> g
                    .field("location")
                    .distance(distance + "km")
                    .location(l -> l.latlon(ll -> ll
                            .lat(lat)
                            .lon(lon)
                    ))
            )._toQuery();

            // 검색 요청 생성
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("es_restaurants")
                    .query(geoDistanceQuery)
                    .size(10)
            );

            // 검색 실행
            SearchResponse<RestaurantDoc> response = elasticsearchClient.search(searchRequest, RestaurantDoc.class);

            // 결과 반환
            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to search nearby restaurants", e);
        }
    }
}
