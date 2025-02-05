package com.ll.commars.domain.restaurant.businessHour.entity;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_business_hours")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessHour extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요일 ( 1: 월요일, 2: 화요일, 3: 수요일, 4: 목요일, 5: 금요일, 6: 토요일, 7: 일요일 )
    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    // 영업 시작 시간
    @Column(name = "open_time")
    private LocalDateTime openTime;

    // 영업 종료 시간
    @Column(name = "close_time")
    private LocalDateTime closeTime;

    // RestaurantBusinessHour와 Restaurant: 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
