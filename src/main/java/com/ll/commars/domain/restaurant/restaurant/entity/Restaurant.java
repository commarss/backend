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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "details", nullable = false)
	private String details;

	// todo: 리뷰 리팩터링 시 계산 추후 구현
	@Column(name = "average_rate", nullable = false)
	@ColumnDefault("0.0")
	private double averageRate = 0.0;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "contact")
	private String contact;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "lat", nullable = false)
	@ColumnDefault("37.0")
	private double lat = 37.0;

	@Column(name = "lon", nullable = false)
	@ColumnDefault("128.0")
	private double lon = 128.0;

	@Column(name = "running_state", nullable = false)
	@ColumnDefault("true")
	private boolean runningState = true;

	// todo: 추후 구현
	@Column(name = "summarized_review")
	private String summarizedReview;

	@Column(name = "restaurant_category")
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

	public void updateLocation(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
	}
}
