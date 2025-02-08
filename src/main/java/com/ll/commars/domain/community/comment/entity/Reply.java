package com.ll.commars.domain.community.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.commars.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "replies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // ✅ 추가
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false) // 부모 댓글 참조
    private Comment comment;
}
