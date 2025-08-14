package com.ll.commars.domain.restaurant.restaurant.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import com.ll.commars.domain.favorite.favorite.entity.FavoriteRestaurant;
import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;
import com.ll.commars.domain.restaurant.menu.entity.Menu;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.global.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Restaurant extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String details;

	// todo: 리뷰 리팩터링 시 계산 추후 구현
	@Column(nullable = false)
	@ColumnDefault("0.0")
	private double averageRate = 0.0;

	@Column
	private String imageUrl;

	@Column
	private String contact;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private Double lat;

	@Column(nullable = false)
	private Double lon;

	@Column(nullable = false)
	@ColumnDefault("true")
	private boolean runningState = true;

	// todo: 추후 구현
	@Column
	private String summarizedReview;

	@Column
	@Enumerated(EnumType.STRING)
	private RestaurantCategory restaurantCategory;

	@BatchSize(size = 20)
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Menu> menus = new ArrayList<>();

	@BatchSize(size = 20)
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BusinessHour> businessHours = new ArrayList<>();

	@BatchSize(size = 20)
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FavoriteRestaurant> favoriteRestaurants = new ArrayList<>();

	public Restaurant(String name, String details, String imageUrl, String contact,
		String address, RestaurantCategory restaurantCategory) {
		this.name = name;
		this.details = details;
		this.imageUrl = imageUrl;
		this.contact = contact;
		this.address = address;
		this.restaurantCategory = restaurantCategory;
	}

	public void updateBusinessHours(List<BusinessHour> newBusinessHours) {
		this.businessHours.clear();
		if (newBusinessHours != null) {
			this.businessHours.addAll(newBusinessHours);
		}
	}

	public void updateRestaurant(String name, String details, String imageUrl,
		String contact, String address, String category) {
		this.name = name;
		this.details = details;
		this.imageUrl = imageUrl;
		this.contact = contact;
		this.address = address;
		this.restaurantCategory = RestaurantCategory.fromString(category);
	}
}
