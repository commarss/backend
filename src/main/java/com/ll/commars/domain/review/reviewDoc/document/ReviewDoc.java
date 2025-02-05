package com.ll.commars.domain.review.reviewDoc.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "es_reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDoc {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Integer)
    private Integer rate;
}
