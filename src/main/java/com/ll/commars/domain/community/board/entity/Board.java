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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

	@Column(nullable = false)
	@ColumnDefault("0")
	private int likeCount = 0;

	@Column(nullable = false)
	@ColumnDefault("0")
	private int dislikeCount = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HashTag> hashTags = new ArrayList<>();

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reaction> reactions = new ArrayList<>();

	public Board(String title, String content, int views, String imageUrl, int likeCount, int dislikeCount,
		User user, List<Comment> comments, List<HashTag> hashTags, List<Reaction> reactions) {
		this.title = title;
		this.content = content;
		this.views = views;
		this.imageUrl = imageUrl;
		this.likeCount = likeCount;
		this.dislikeCount = dislikeCount;
		this.user = user;
		this.comments = comments;
		this.hashTags = hashTags;
		this.reactions = reactions;
	}

	public Board(String title, String content, String imageUrl, User user, List<HashTag> hashTags) {
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
		this.user = user;
		this.hashTags = hashTags;
	}

	public void updateBoard(String title, String content, List<HashTag> hashTags) {
		this.title = title;
		this.content = content;
		this.hashTags = hashTags;
	}

	public void incrementViews() {
		this.views++;
	}
}
