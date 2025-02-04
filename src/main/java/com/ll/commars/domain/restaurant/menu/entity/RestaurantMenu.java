package com.ll.commars.domain.restaurant.menu.entity;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "restaurants_menus")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantMenu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 메뉴 이름
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private String price;

    // 메뉴 사진
    @Column(name = "image_url")
    private String imageUrl;

    // menus와 restaurants: 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
