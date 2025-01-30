package com.ll.commars.domain.home.TodayRandom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private double rating;
    private String review;
    private String category;
    private double latitude;
    private double longitude;
    private String keywords;
    private String reviewer;

    // id를 매개변수로 받는 생성자 추가
    public Restaurant(Long id) {
        this.id = id;
    }

    // 기본 생성자 필요 (JPA 사용 시)
    public Restaurant() {
    }


    // 나머지 필드도 마찬가지로 추가
}
