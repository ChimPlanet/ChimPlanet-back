package com.wak.chimplanet.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(description = "게시글 정보")
@Builder // 임시 추가 -> DTO 로 옮겨야함
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

    @Column(name = "like_count")
    @ApiModelProperty(value = "좋아요 개수", example = "10")
    private String likeCount;

    @Column(name = "read_count")
    @ApiModelProperty(value = "조회수", example = "100")
    private String readCount;

    @Column(name = "reg_date")
    // @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "작성일시", example = "2022-03-05 14:30")
    private String regDate;

    @Column(name = "thumbnail_url")
    @ApiModelProperty(value = "썸네일 URL")
    private String thumbnailURL;

    @Column(name = "redirect_url")
    @ApiModelProperty(value = "리다이렉트 URL", example = "https://cafe.naver.com/steamindiegame/1")
    private String redirectURL;

    @Column(name = "is_end")
    @ApiModelProperty(value = "공고 마갑여부", example = "ING || END")
    private String isEnd;

    @Builder.Default
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<BoardTag> boardtags = new ArrayList<>();
}
