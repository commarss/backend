package com.ll.commars.domain.review.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "es_reviews")
@Setting(settingPath = "elasticsearch/review/settings.json")
@Mapping(mappingPath = "elasticsearch/review/mappings.json")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDoc {

	@Id
	private String id;

	@Field(type = FieldType.Long)
	private Long memberId;

	@Field(type = FieldType.Text)
	private String title;

	@Field(type = FieldType.Text)
	private String body;

	@Field(type = FieldType.Integer)
	private Integer rate;

	public ReviewDoc(String id, Long memberId, String title, String body, Integer rate) {
		this.id = id;
		this.memberId = memberId;
		this.title = title;
		this.body = body;
		this.rate = rate;
	}
}
