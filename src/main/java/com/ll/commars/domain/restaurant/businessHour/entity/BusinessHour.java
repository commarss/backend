package com.ll.commars.domain.restaurant.businessHour.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.global.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BusinessHour extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	@Column
	private LocalTime openTime;

	@Column
	private LocalTime closeTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	public BusinessHour(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, Restaurant restaurant) {
		this.dayOfWeek = dayOfWeek;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.restaurant = restaurant;
	}

	public void update(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {
		this.dayOfWeek = dayOfWeek;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}
}
