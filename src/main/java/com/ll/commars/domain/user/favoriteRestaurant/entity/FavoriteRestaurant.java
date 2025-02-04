package com.ll.commars.domain.user.favoriteRestaurant.entity;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.user.favorite.entity.Favorite;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorites_restaurants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRestaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FavoriteRestaurant와 Favorite: 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_id")
    private Favorite favorite;

    // FavoriteRestaurant와 Restaurant: 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
