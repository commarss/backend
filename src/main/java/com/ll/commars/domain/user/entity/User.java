package com.ll.commars.domain.user.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.favorite.favorite.entity.Favorite;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.global.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Enumerated(EnumType.STRING)
	private SocialProvider socialProvider;

	@Column
	private String email;

	@Column
	private String name;

	@Column
	private String password;

	@Column
	private String phoneNumber;

	@Column
	private String profileImageUrl;

	@Column
	private LocalDateTime birthDate;

	@Column
	@Enumerated(EnumType.STRING)
	private Gender gender;

	// User와 Review: 일대다
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonIgnore
	private List<Review> reviews;

	// User와 Favorite: 일대다
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Favorite> favorites;

	// User와 Board: 일대다
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Post> posts;

	// User와 Comment: 일대다
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Comment> comments;
}
