package com.ll.commars.domain.user.favorite.entity;

import java.util.ArrayList;
import java.util.List;

import com.ll.commars.domain.user.favoriteRestaurant.entity.FavoriteRestaurant;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.global.baseEntity.BaseEntity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 찜 리스트 이름
	@Column(name = "name")
	private String name;

	// 찜 리스트 공개 여부
	@Column(name = "is_public", nullable = false)
	private Boolean isPublic = true;

	// Favorite과 User: 다대일
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// Favorite과 FavoriteRestaurant: 일대다
	@OneToMany(mappedBy = "favorite", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<FavoriteRestaurant> favoriteRestaurants = new ArrayList<>();  // ✅ 초기화 추가!
}
