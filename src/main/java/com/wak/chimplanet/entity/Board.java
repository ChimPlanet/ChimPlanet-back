package com.wak.chimplanet.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.ColumnDefault;


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
    private Integer readCount;

    @Column(name = "reg_date")
    @ApiModelProperty(value = "작성일시", example = "2022-03-05 14:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime regDate;

    @Column(name = "thumbnail_url", length = 1000)
    @ApiModelProperty(value = "썸네일 URL")
    private String thumbnailURL;

    @Column(name = "redirect_url", length = 1000)
    @ApiModelProperty(value = "리다이렉트 URL", example = "https://cafe.naver.com/steamindiegame/1")
    private String redirectURL;

    @Column(name = "is_end")
    @ApiModelProperty(value = "공고 마감여부", example = "ING || END")
    private String isEnd;

    @Column(name = "unauthorized")
    @ColumnDefault("N")
    @ApiModelProperty(value = "접근 권한 필요 게시물 여부", example = "Y || N, default = N")
    private String unauthorized;

    @Builder.Default
    // @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "article_id")
    private List<BoardTag> boardTags = new ArrayList<>();

    /**
     * 연관관계 메서드
     */
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
    public static Board createBoardWithTag(Board board, List<BoardTag> boardTags, String unauthorized) {
        return Board.builder()
                .articleId(board.articleId)
                .writer(board.writer)
                .boardTitle(board.boardTitle)
                .writer(board.writer)
                .articleId(board.articleId)
                .isEnd(board.isEnd)
                .redirectURL(board.redirectURL)
                .readCount(board.readCount)
                .regDate(board.regDate)
                .thumbnailURL(board.thumbnailURL)
                .unauthorized(unauthorized)
                .boardTags(boardTags)
                .build();
    }

    /**
     * update : 변경감지
     */
    public void updateBoard(Board board, List<BoardTag> boardTags) {
        this.boardTitle = board.boardTitle;
        this.readCount = board.readCount;
        this.thumbnailURL = board.thumbnailURL;
        this.isEnd = board.isEnd;
        this.boardTags = boardTags;
        this.unauthorized = board.unauthorized;
    }
    
    /**
     * admin update : 변경감지
     * 태그정보, 마감여부 수정
     */
    public void updateAdminBoard(String isEnd, List<BoardTag> boardTags) {
        this.isEnd = isEnd;
        this.boardTags = boardTags;
    }

    /**
     *  boardTags 리스트에서 해당 BoardTag를 삭제한 후, BoardTag의 Board 정보를 null로 변경합니다.
     */
    public void removeBoardTag(BoardTag boardTag) {
        if (this.boardTags.remove(boardTag)) {
            boardTag.setBoard(null);
        }
    }

}
