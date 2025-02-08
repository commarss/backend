package com.ll.commars.domain.user.favorite.entity;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.user.favoriteRestaurant.entity.FavoriteRestaurant;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "favorites")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 찜 리스트 이름
    @Column(name = "name")
    private String name;

    // 찜 리스트 공개 여부
    @Column(name = "is_public")
    private Boolean isPublic;

    // Favorite과 User: 다대일
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Favorite과 FavoriteRestaurant: 일대다
    @OneToMany(mappedBy = "favorite", fetch = FetchType.LAZY)
    private List<FavoriteRestaurant> favoriteRestaurants;
}
