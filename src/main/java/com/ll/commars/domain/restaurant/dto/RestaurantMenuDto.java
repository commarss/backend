package com.ll.commars.domain.restaurant.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class RestaurantMenuDto {

	// 메뉴 정보
	@Getter
	@Builder
	public static class MenuInfo {

		private Long id;
		private String name;
		private Integer price;
		private String imageUrl;
	}

	// 메뉴 등록 및 수정 시 응답
	@Getter
	@Builder
	public static class MenuWriteResponse {

		private String restaurantName;
		private String name;
		private Integer price;
	}

	// 식당의 모든 메뉴 조회 시 응답
	@Getter
	@Builder
	public static class ShowAllMenusResponse {

		private List<MenuInfo> menus;
	}
}
