package com.ll.commars.domain.review.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(indexName = "es_reviews", createIndex = true)
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
}
