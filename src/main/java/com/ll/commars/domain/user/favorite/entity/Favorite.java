package com.ll.commars.domain.user.favorite.entity;

import com.ll.commars.domain.restaurants.restaurants.entity.Restaurants;
import com.ll.commars.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "myfavorite") //찜테이블
@Getter
@Setter
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurants restaurants;  // 식당과 연결

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // User 엔티티와 연결 (email을 외래키로 사용)
}
