package com.ll.commars.domain.restaurant.restaurant.entity;

import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "average_rate")
    private Double averageRate;

    @Column(name = "image_url")
    private String imageUrl;

    // 식당 전화 번호
    @Column(name = "contact")
    private String contact;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    // 위도
    @NotNull
    @Column(name = "lat", nullable = false)
    private Double lat;

    // 경도
    @NotNull
    @Column(name = "lng", nullable = false)
    private Double lng;

    // 식당 영업 여부
    @Column(name = "running_state")
    private Boolean runningState;

    // 요약 리뷰
    @Column(name = "summarized_review")
    private String summarizedReview;
}
