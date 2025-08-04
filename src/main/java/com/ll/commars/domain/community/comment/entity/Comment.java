package com.ll.commars.domain.community.comment.entity;

import java.util.ArrayList;
import java.util.List;

import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.user.user.entity.User;

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

	// Comment와 User: 다대일
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	// Comment와 Board: 다대일

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reply> replies = new ArrayList<>();

	//추가
	// ✅ 부모 댓글 (대댓글 구현을 위해 자기 자신을 참조)
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore // 순환 참조 방지
    private Comment parentComment;

     */
	// ✅ 대댓글 리스트 (부모 댓글이 없는 경우 일반 댓글)
    /*
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

     */
}
