package com.ll.commars.domain.community.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ll.commars.domain.community.comment.entity.Comment;

import com.ll.commars.domain.community.reaction.entity.Reaction;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "boards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // int → Long 변경

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String content;

    @NotNull
    @Column(nullable = false)
    private Integer views= 0;;

    @Column(name = "image_url")
    private String imageUrl;

    // Board와 User: 다대일 관계 (게시글 작성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    // ✅ HashTags를 ','로 구분된 문자열로 저장
    @Column(name = "hash_tags", length = 1000)
    private String hashTags;

    public void setHashTags(List<String> tags) {
        this.hashTags = String.join(",", tags);
    }

    public List<String> getHashTags() {
        return hashTags != null ? List.of(hashTags.split("\\s*,\\s*")) : List.of();
    }


    //추가
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions = new ArrayList<>();
    @Builder.Default
    @Column(nullable = false)
    private Integer likeCount = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer dislikeCount = 0;


    // ✅ 좋아요 추가
    public void incrementLikes() {
        this.likeCount += 1;
    }

    // ✅ 싫어요 추가
    public void incrementDislikes() {
        this.dislikeCount += 1;
    }

    // ✅ 좋아요 감소
    public void decrementLikes() {
        if (this.likeCount > 0) {
            this.likeCount -= 1;
        }
    }

    // ✅ 싫어요 감소
    public void decrementDislikes() {
        if (this.dislikeCount > 0) {
            this.dislikeCount -= 1;
        }
    }
}


/*
// Board 엔티티 수정
@Entity
@Table(name = "boards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String content;

    // 게시글 조회수
    @NotNull
    @Column(nullable = false)
    private Integer views;

    // 게시글 이미지
    @Column(name = "image_url")
    private String imageUrl;

//    public void increaseLikes() {
//        this.likes++;
//    }

    // Board와 User: 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    public void addHashTags(List<HashTag> tags) {
//        this.hashTags.addAll(tags);
//    }

    // Board와 Comment: 일대다
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();
}*/