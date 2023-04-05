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
    private Long boardTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagObj tagObj;

    // 생성메서드
    public static BoardTag createBoardTag(TagObj tag, Board board) {
        BoardTag boardTag = BoardTag.builder()
                .board(board)
                .tagObj(tag)
                .build();
        board.addBoardTag(boardTag);
        return boardTag;
    }

    public static BoardTag addBoardTag(TagObj tag, Board board) {
        return BoardTag.builder()
                .board(board)
                .tagObj(tag)
                .build();
    }
}
