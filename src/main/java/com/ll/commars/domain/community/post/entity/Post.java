package com.ll.commars.domain.community.post.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.global.BaseEntity;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostHashTag> postHashTags = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostLike> postLikes = new ArrayList<>();

	public Post(String title, String content, int views, String imageUrl, int likeCount,
		Member member, List<Comment> comments, List<PostHashTag> postHashTags, List<PostLike> postLikes) {
		this.title = title;
		this.content = content;
		this.views = views;
		this.imageUrl = imageUrl;
		this.likeCount = likeCount;
		this.member = member;
		this.comments = comments;
		this.postHashTags = postHashTags;
		this.postLikes = postLikes;
	}

	public Post(String title, String content, String imageUrl, Member member) {
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
		this.member = member;
	}

	public void updatePost(String title, String content, String imageUrl, List<PostHashTag> postHashTags) {
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
		this.postHashTags = postHashTags;
	}

	public void incrementViews() {
		this.views++;
	}

	public void addHashTags(List<PostHashTag> hashTags) {
		this.postHashTags.clear();
		this.postHashTags.addAll(hashTags);
		hashTags.forEach(hashTag -> hashTag.setPost(this));
	}

	public void addLike() {
		this.likeCount++;
	}
}
