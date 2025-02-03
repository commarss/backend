package com.ll.commars.domain.community.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.boardHashTag.entity.HashTag;
import com.ll.commars.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Board 엔티티 수정
@Entity
@Getter
@Setter
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int likes = 0;  // 좋아요 수 추가

    public void increaseLikes() {
        this.likes++;
    }

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime regdate;

    @Column(nullable = false)
    private int viewCnt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true) // 물리적 컬럼 이름은 그대로 유지
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HashTag> hashTags = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("board")  // 순환 참조 방지
    private List<Comment> comments = new ArrayList<>();

    // 태그 목록
    @Transient
    private List<String> tags;

    public void addHashTags(List<HashTag> tags) {
        this.hashTags.addAll(tags);
    }
}