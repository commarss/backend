package com.ll.commars.domain.user.user.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ll.commars.domain.user.favorite.dto.FavoriteDto;

import lombok.Builder;
import lombok.Getter;

public class UserDto {

	@Getter
	@Builder
	public static class UserInfo {

		private Long id;
		private Integer socialProvider;
		private String email;
		private String name;
		private String password;
		private String loginId;
		private String phoneNumber;
		private String profileImageUrl;
		private LocalDateTime birthDate;
	}

	@Getter
	@Builder
	public static class UserFavoriteListsResponse {

		private Long id; // 사용자 ID
		private String name; // 사용자 이름
		private List<FavoriteDto.FavoriteInfo> favoriteLists;
	}
}

