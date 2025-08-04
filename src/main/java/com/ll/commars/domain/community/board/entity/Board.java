package com.ll.commars.domain.community.board.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.reaction.entity.Reaction;
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
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	@ColumnDefault("0")
	private int views = 0;

	@Column
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Comment> comments = new ArrayList<>();

	// ✅ HashTags를 ','로 구분된 문자열로 저장
	// 태그 CRUD가 복잡하므로 추후 테이블로 분리해야 함.
	@Column(name = "hash_tags", length = 1000)
	private String hashTags;

	public void setHashTags(List<String> tags) {
		this.hashTags = String.join(",", tags);
	}

	public List<String> getHashTags() {
		return hashTags != null ? List.of(hashTags.split("\\s*,\\s*")) : List.of();
	}

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Reaction> reactions = new ArrayList<>();

	@Column(nullable = false)
	@ColumnDefault("0")
	private int likeCount = 0;

	@Column(nullable = false)
	@ColumnDefault("0")
	private int dislikeCount = 0;
}
