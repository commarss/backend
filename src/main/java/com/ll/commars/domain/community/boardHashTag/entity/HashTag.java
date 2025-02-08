package com.ll.commars.domain.community.boardHashTag.entity;

/*
import com.ll.commars.domain.community.board.entity.Board;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "hash_tag")
@Setter
@Getter
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)  // ✅ Board 저장 시 HashTag도 자동 저장
    @JoinColumn(name = "board_Id")
    private Board board;
}
*/