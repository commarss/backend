package com.ll.commars.domain.review.review.entity;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.user.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "body")
    private String body;

    @NotNull
    @Column(name = "rate")
    private Integer rate;

    // Review와 Restaurant: 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // Review와 User: 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
