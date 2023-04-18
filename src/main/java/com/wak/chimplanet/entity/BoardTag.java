package com.wak.chimplanet.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void updateBoardTags(Board board, TagObj tag) {
        this.board = board;
        this.tagObj = tag;
    }
}