package com.ll.commars.domain.community.reaction.entity;

import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"board_id", "user_id"}) // 동일한 게시글에 같은 유저가 중복 반응하지 못하도록 설정
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ 게시글과의 관계 설정 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // ✅ 유저와의 관계 설정 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ 좋아요(true) / 싫어요(false)
    @Column(nullable = false)
    private boolean liked;
}
