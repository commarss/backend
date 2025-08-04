package com.ll.commars.domain.community.comment.entity;

import com.ll.commars.domain.community.post.entity.Post;
import com.ll.commars.domain.user.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
}
