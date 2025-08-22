package com.ll.commars.domain.review.entity;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.global.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "body", nullable = false)
	private String body;

	@NotNull
	@Column(name = "rate", nullable = false)
	private Integer rate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public Review(String title, String body, Integer rate, Restaurant restaurant, Member member) {
		this.title = title;
		this.body = body;
		this.rate = rate;
		this.restaurant = restaurant;
		this.member = member;
	}

	public void update(String title, String body, Integer rate) {
		this.title = title;
		this.body = body;
		this.rate = rate;
	}
}
