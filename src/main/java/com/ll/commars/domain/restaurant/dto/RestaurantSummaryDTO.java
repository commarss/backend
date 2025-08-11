package com.ll.commars.domain.restaurant.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ll.commars.domain.restaurant.entity.BusinessHour;
import com.ll.commars.domain.restaurant.entity.RestaurantMenu;
import com.ll.commars.domain.restaurant.entity.Restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSummaryDTO {

	private Long id;
	private String name;
	private String contact;
	private String details;
	private Double averageRate;
	private String summarizedReview;
	private String imageUrl;
	private String categoryName;
	private List<MenuDTO> menus;
	private List<BusinessHourDTO> businessHours;

	public static RestaurantSummaryDTO fromEntity(Restaurant restaurant) {
		return new RestaurantSummaryDTO(
			restaurant.getId(),
			restaurant.getName(),
			restaurant.getContact(),
			restaurant.getDetails(),
			restaurant.getAverageRate(),
			restaurant.getSummarizedReview(),
			restaurant.getImageUrl(),
			restaurant.getRestaurantCategory() != null ? restaurant.getRestaurantCategory().getName() : null,
			restaurant.getRestaurantMenus().stream().map(MenuDTO::fromEntity).collect(Collectors.toList()),
			restaurant.getBusinessHours().stream().map(BusinessHourDTO::fromEntity).collect(Collectors.toList())
		);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MenuDTO {

		private String name;
		private Integer price;
		private String imageUrl;

		public static MenuDTO fromEntity(RestaurantMenu menu) {
			return new MenuDTO(menu.getName(), menu.getPrice(), menu.getImageUrl());
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class BusinessHourDTO {

		private Integer dayOfWeek;
		private LocalDateTime openTime;
		private LocalDateTime closeTime;

		public static BusinessHourDTO fromEntity(BusinessHour businessHour) {
			return new BusinessHourDTO(businessHour.getDayOfWeek(), businessHour.getOpenTime(),
				businessHour.getCloseTime());
		}
	}
}


/*
private String imageUrl;
restaurant.getImageUrl(),
 */