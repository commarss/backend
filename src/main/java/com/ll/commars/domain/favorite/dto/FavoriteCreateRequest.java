package com.ll.commars.domain.favorite.dto;

import jakarta.validation.constraints.NotBlank;

public record FavoriteCreateRequest(
	@NotBlank(message = "찜 리스트 이름은 필수입니다.")
	String name,

	boolean isPublic
) {
}
