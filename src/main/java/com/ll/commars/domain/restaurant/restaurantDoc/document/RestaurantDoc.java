package com.ll.commars.domain.restaurant.restaurantDoc.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "es_restaurants", createIndex = true)
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/mappings.json")
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

   @JsonProperty("average_rate")
   @Field(name = "average_rate", type = FieldType.Double)
   private Double averageRate;

   @GeoPointField
   private String location;

   @Field(type = FieldType.Double)
   private Double lat;

   @Field(type = FieldType.Double)
   private Double lon;
}
