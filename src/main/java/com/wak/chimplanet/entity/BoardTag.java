package com.wak.chimplanet.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board_tag")
@Builder
public class BoardTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_tag_id", nullable = false)
    private Integer boardTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // 생성메서드
    public static BoardTag createBoardTag(Tag tag, Board board) {
        BoardTag boardTag = BoardTag.builder()
                .board(board)
                .tag(tag)
                .build();
        board.addBoardTag(boardTag);
        return boardTag;
    }

    public static BoardTag addBoardTag(Tag tag, Board board) {
        return BoardTag.builder()
                .board(board)
                .tag(tag)
                .build();
    }
}
