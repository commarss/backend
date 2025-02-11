package com.ll.commars.domain.community.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ll.commars.domain.community.comment.entity.Comment;

import com.ll.commars.domain.community.reaction.entity.Reaction;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String content;

    @NotNull
    @Column(nullable = false)
    @ColumnDefault("0") // 객체 생성 시점이 아닌(Builder.Default) 테이블 생성 시점에 views의 기본 값 세팅
    private Integer views;

    @Column(name = "image_url")
    private String imageUrl;

    // Board와 User: 다대일 관계 (게시글 작성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Board와 Comment: 일대다 관계
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
    private Integer likeCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int dislikeCount;  // 🚀 기본값 0 설정

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
