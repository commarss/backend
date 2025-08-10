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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member", uniqueConstraints = {
	@UniqueConstraint(
		name = "uk_member_email_auth_type",
		columnNames = {"email", "authType"}
	)
})
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Enumerated(EnumType.STRING)
	private AuthType authType;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
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

	public Member(String email, String name, AuthType authType) {
		this.email = email;
		this.name = name;
		this.authType = authType;
	}

	public Member(String email, String name, String password, AuthType authType) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.authType = authType;
	}
}
