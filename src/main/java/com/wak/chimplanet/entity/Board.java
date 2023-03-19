package com.wak.chimplanet.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.tomcat.util.digester.ArrayStack;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "board")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(description = "게시글 정보")
@Builder // 임시 추가 -> DTO 로 옮겨야함
@ToString
public class Board {

    @Id
    @Column(name = "article_id")
    @ApiModelProperty(value = "게시글 아이디")
    private String articleId;

    @Column(name = "writer")
    @ApiModelProperty(value = "작성자")
    private String writer;

    @Column(name = "board_title")
    @ApiModelProperty(value = "게시글 제목")
    private String boardTitle;

    @Column(name = "read_count")
    @ApiModelProperty(value = "조회수", example = "100")
    private String readCount;

    @Column(name = "reg_date")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "작성일시", example = "2022-03-05 14:30")
    private String regDate;

    @Column(name = "thumbnail_url", length = 1000)
    @ApiModelProperty(value = "썸네일 URL")
    private String thumbnailURL;

    @Column(name = "redirect_url", length = 1000)
    @ApiModelProperty(value = "리다이렉트 URL", example = "https://cafe.naver.com/steamindiegame/1")
    private String redirectURL;

    @Column(name = "is_end")
    @ApiModelProperty(value = "공고 마갑여부", example = "ING || END")
    private String isEnd;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<BoardTag> boardTags = new ArrayList<>();

    @Column(name = "unauthorized")
    // @ColumnDefault("Y")
    @ApiModelProperty(value = "접근 권한 필요 게시물 여부", example = "Y || N, default = Y")
    private String unauthorized;

    // private String official; [공식] 모집 게시물

    // 연관관계 메서드
    public void addBoardTag(BoardTag boardTag) {
        if(this.boardTags == null) {
            boardTags = new ArrayList<>();
        }
        boardTags.add(boardTag);
        boardTag.setBoard(this);
    }

    /**
     * 생성메서드
     */
    public static Board createBoardWithTag(Board board, List<BoardTag> boardTags) {
        return Board.builder()
                .articleId(board.articleId)
                .writer(board.articleId)
                .boardTitle(board.boardTitle)
                .writer(board.writer)
                .articleId(board.articleId)
                .isEnd(board.isEnd)
                .redirectURL(board.redirectURL)
                .readCount(board.readCount)
                .regDate(board.regDate)
                .thumbnailURL(board.thumbnailURL)
                .unauthorized(board.unauthorized)
                .boardTags(boardTags)
                .build();
    }


}
