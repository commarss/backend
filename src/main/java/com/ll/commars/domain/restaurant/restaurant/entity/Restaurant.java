package com.ll.commars.domain.restaurant.restaurant.entity;

import java.util.ArrayList;
import java.util.List;

import com.ll.commars.domain.favorite.favoriteRestaurant.entity.FavoriteRestaurant;
import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;
import com.ll.commars.domain.restaurant.category.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.global.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull
	@Column(name = "details")
	private String details;

	@Column(name = "average_rate")
	private Double averageRate;

	@Column(name = "image_url")
	private String imageUrl;

	// 식당 전화 번호
	@Column(name = "contact")
	private String contact;

	@NotNull
	@Column(name = "address", nullable = false)
	private String address;

	// 위도
	@NotNull
	@Column(name = "lat", nullable = false)
	private Double lat;

	// 경도
	@NotNull
	@Column(name = "lon", nullable = false)
	private Double lon;

	// 식당 영업 여부
	@Column(name = "running_state")
	private Boolean runningState;

	// 요약 리뷰
	@Column(name = "summarized_review")
	private String summarizedReview;

	// Restaurant와 RestaurantMenu: 일대다
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<RestaurantMenu> restaurantMenus;

	// Restaurant와 RestaurantCategory: 다대일
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_category_id")
	private RestaurantCategory restaurantCategory;

	// Restaurant와 RestaurantBusinessHours: 일대다
	@Builder.Default
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BusinessHour> businessHours = new ArrayList<>();

	// Restaurant와 Review: 일대다
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews;

	// Restaurant와 FavoriteRestaurant: 일대다
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FavoriteRestaurant> favoriteRestaurants;

	public void setCategory(RestaurantCategory category) {
		if (this.restaurantCategory != null) {
			this.restaurantCategory.getRestaurants().remove(this);
		}
		this.restaurantCategory = category;
		if (category != null) {
			category.getRestaurants().add(this);
		}
	}

	public void setBusinessHours(List<BusinessHour> businessHours) {
		this.businessHours.clear();
		if (businessHours != null) {
			this.businessHours.addAll(businessHours);
			businessHours.forEach(hour -> hour.setRestaurant(this));
		}
	}
}
