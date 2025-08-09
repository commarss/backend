package com.ll.commars.domain.member.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.favorite.favorite.entity.Favorite;
import com.ll.commars.domain.review.entity.Review;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Enumerated(EnumType.STRING)
	private SocialProvider socialProvider;

	@Column(unique = true)
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

	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonIgnore
	private List<Review> reviews;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Favorite> favorites;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Post> posts;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Comment> comments;

	public Member(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}
}
