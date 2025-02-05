package com.ll.commars.domain.restaurant.restaurantDoc.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "es_restaurants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDoc {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String details;

    @Field(name = "average_rate", type = FieldType.Double)
    private Double averageRate;
}
