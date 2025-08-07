package com.ll.commars.domain.community.post.entity;

import com.ll.commars.domain.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_like", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"post_id", "user_id"})
})
@Getter
@NoArgsConstructor
public class PostLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Member member;

	public PostLike(Post post, Member member) {
		this.post = post;
		this.member = member;
	}
}
