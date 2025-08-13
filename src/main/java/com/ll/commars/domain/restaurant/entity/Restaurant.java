package com.ll.commars.domain.restaurant.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.ll.commars.domain.favorite.favorite.entity.FavoriteRestaurant;
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

	@Column
	private Double averageRate;

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

	@Column
	private Boolean runningState;

	@Column
	private String summarizedReview;

	@Column
	@Enumerated(EnumType.STRING)
	private RestaurantCategory restaurantCategory;

	@BatchSize(size = 20)
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RestaurantMenu> restaurantMenus;

	@BatchSize(size = 20)
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BusinessHour> businessHours = new ArrayList<>();

	@BatchSize(size = 20)
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FavoriteRestaurant> favoriteRestaurants;

	public Restaurant(String name, String details, String imageUrl, String contact,
			String address, RestaurantCategory restaurantCategory) {
		this.name = name;
		this.details = details;
		this.imageUrl = imageUrl;
		this.contact = contact;
		this.address = address;
		this.restaurantCategory = restaurantCategory;
	}

	public void setBusinessHours(List<BusinessHour> businessHours) {
		this.businessHours.clear();
		if (businessHours != null) {
			this.businessHours.addAll(businessHours);
			businessHours.forEach(hour -> hour.setRestaurant(this));
		}
	}

	public void updateRestaurant(String name, String category) {
		this.name = name;
		this.restaurantCategory = RestaurantCategory.fromString(category);
	}
}
