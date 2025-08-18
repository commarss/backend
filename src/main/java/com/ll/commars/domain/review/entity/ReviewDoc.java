package com.ll.commars.domain.review.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "es_reviews", createIndex = true)
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewDoc {

	@Id
	private String id;

	@Field(type = FieldType.Text)
	private String name;

	@Field(type = FieldType.Text)
	private String body;

	@Field(type = FieldType.Integer)
	private Integer rate;
}
