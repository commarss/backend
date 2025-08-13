package com.ll.commars.domain.restaurant.entity;

import com.ll.commars.global.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Menu extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer price;

	@Column
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	public Menu(String name, String imageUrl, Integer price, Restaurant restaurant) {
		this.name = name;
		this.imageUrl = imageUrl;
		this.price = price;
		this.restaurant = restaurant;
	}

	public void update(String name, String imageUrl, Integer price) {
		this.name = name;
		this.imageUrl = imageUrl;
		this.price = price;
	}
}
