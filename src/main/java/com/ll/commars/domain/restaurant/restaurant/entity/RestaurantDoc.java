package com.ll.commars.domain.restaurant.restaurant.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "es_restaurants")
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/mappings.json")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	@Field(type = FieldType.Text)
	private String address;

	@Field(type = FieldType.Text)
	private String category;

	@GeoPointField
	private String location;

	@Field(type = FieldType.Double)
	private Double lat;

	@Field(type = FieldType.Double)
	private Double lon;
}
