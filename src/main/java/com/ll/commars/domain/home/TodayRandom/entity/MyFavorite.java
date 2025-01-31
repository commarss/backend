package com.ll.commars.domain.home.TodayRandom.entity;

import com.ll.commars.domain.home.board.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "myfavorite") //찜테이블
@Getter
@Setter
public class MyFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;  // 식당과 연결

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // User 엔티티와 연결 (email을 외래키로 사용)
}
