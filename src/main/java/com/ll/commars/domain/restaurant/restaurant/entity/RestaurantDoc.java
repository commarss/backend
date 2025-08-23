package com.ll.commars.domain.restaurant.restaurant.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "es_restaurants")
@Setting(settingPath = "elasticsearch/restaurant/settings.json")
@Mapping(mappingPath = "elasticsearch/restaurant/mappings.json")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantDoc {

	@Id
	private String id;

	@Field(name = "name", type = FieldType.Text)
	private String name;

	@Field(name = "details", type = FieldType.Text)
	private String details;

	@Field(name = "average_rate", type = FieldType.Double)
	private Double averageRate;

	@Field(name = "address", type = FieldType.Text)
	private String address;

	@Field(name = "restaurant_category", type = FieldType.Keyword)
	private RestaurantCategory restaurantCategory;

	@GeoPointField
	private GeoPoint location;
}
