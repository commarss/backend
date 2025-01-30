package com.ll.commars.domain.home.TodayRandom.entity;
import com.ll.commars.domain.home.board.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mypage")
@Getter
@Setter
public class Mypage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    private String reviewer; // 리뷰어
    private String keywords; // 키워드
    private String review;   // 리뷰 내용
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private double score;    // 리뷰 점수 (0.0 ~ 5.0)
}
