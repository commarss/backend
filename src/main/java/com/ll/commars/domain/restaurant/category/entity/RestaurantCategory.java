package com.ll.commars.domain.restaurant.category.entity;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // RestaurantCategory와 Restaurant: 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
