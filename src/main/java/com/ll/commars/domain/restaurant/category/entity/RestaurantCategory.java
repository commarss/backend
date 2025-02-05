package com.ll.commars.domain.restaurant.category.entity;

import com.ll.commars.domain.restaurant.businessHour.entity.RestaurantBusinessHour;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    // RestaurantCategory와 Restaurant: 일대다
    @OneToMany(mappedBy = "restaurantCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Restaurant> restaurants;

    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
        restaurant.setRestaurantCategory(this);
    }

    public void removeRestaurant(Restaurant restaurant) {
        this.restaurants.remove(restaurant);
        restaurant.setRestaurantCategory(null);
    }
}
