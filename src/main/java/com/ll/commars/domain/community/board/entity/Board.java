package com.ll.commars.domain.community.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.community.boardHashTag.entity.HashTag;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}