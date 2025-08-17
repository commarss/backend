package com.ll.commars.domain.restaurant.restaurant.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;

import co.elastic.clients.elasticsearch._types.DistanceUnit;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RestaurantDocRepositoryImpl implements RestaurantDocRepositoryCustom {

    private final ElasticsearchOperations operations;

    @Override
    public List<RestaurantDoc> searchByKeywordAndLocation(String keyword, GeoPoint location, double distance) {
        // 키워드 검색 쿼리
        MultiMatchQuery textQuery = MultiMatchQuery.of(m -> m
            .query(keyword)
            .fields("name^2", "details")
            .operator(Operator.Or)
            .fuzziness("AUTO")
        );

        // 거리 필터링 쿼리
        GeoDistanceQuery geoFilter = GeoDistanceQuery.of(g -> g
            .field("location")
            .location(l -> l.latlon(d -> d.lat(location.getLat()).lon(location.getLon())))
            .distance(String.format("%.0fm", distance))
        );

        // 최종 쿼리
        BoolQuery boolQuery = BoolQuery.of(b -> b
            .must(textQuery._toQuery())
            .filter(geoFilter._toQuery())
        );

        Query query = NativeQuery.builder()
            .withQuery(q -> q
                .functionScore(fs -> fs
                    .query(boolQuery._toQuery())
                    .functions(f -> f
                        .fieldValueFactor(fv -> fv
                            .field("average_rate")
                            .factor(1.5)
                            .modifier(FieldValueFactorModifier.Sqrt)
                            .missing(0.0)
                        )
                    )
                    .boostMode(FunctionBoostMode.Multiply)
                )
            )
            .build();

        return operations.search(query, RestaurantDoc.class)
            .stream()
            .map(SearchHit::getContent)
            .toList();
    }

    @Override
    public List<RestaurantDoc> findNearbyRestaurantsSortedByDistance(GeoPoint location, double distance) {
        Query query = NativeQuery.builder()
            .withQuery(q -> q
                .bool(b -> b
                    .filter(f -> f
                        .geoDistance(g -> g
                            .field("location")
                            .location(l -> l.latlon(d -> d.lat(location.getLat()).lon(location.getLon())))
                            .distance(String.format("%.0fm", distance))
                        )
                    )
                )
            )
            .withSort(s -> s
                .geoDistance(g -> g
                    .field("location")
                    .location(l -> l.latlon(d -> d.lat(location.getLat()).lon(location.getLon())))
                    .order(SortOrder.Asc)
                    .unit(DistanceUnit.Meters)
                )
            )
            .build();

        return operations.search(query, RestaurantDoc.class)
            .stream()
            .map(SearchHit::getContent)
            .toList();
    }
}
