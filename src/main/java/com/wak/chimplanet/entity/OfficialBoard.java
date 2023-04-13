package com.wak.chimplanet.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "official_board")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "공식 게시글 정보")
@Builder // 임시 추가 -> DTO 로 옮겨야함
public class OfficialBoard{

    @Id
    @Column(name = "article_id")
    @ApiModelProperty(value = "공식 게시글 아이디")
    private String articleId;

    @Column(name = "writer")
    @ApiModelProperty(value = "작성자")
    private String writer;

    @Column(name = "board_title")
    @ApiModelProperty(value = "공식 게시글 제목")
    private String boardTitle;

    @Column(name = "read_count")
    @ApiModelProperty(value = "조회수", example = "100")
    private String readCount;

    @Column(name = "reg_date")
    @ApiModelProperty(value = "작성일시", example = "2022-03-05 14:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime regDate;

    @Column(name = "thumbnail_url", length = 1000)
    @ApiModelProperty(value = "썸네일 URL")
    private String thumbnailURL;

    @Column(name = "redirect_url", length = 1000)
    @ApiModelProperty(value = "리다이렉트 URL", example = "https://cafe.naver.com/steamindiegame/1")
    private String redirectURL;

    @Column(name = "is_end")
    @ApiModelProperty(value = "공고 마갑여부", example = "ING || END")
    private String isEnd;

    @Builder.Default
    @OneToMany(mappedBy = "tagObj", cascade = CascadeType.ALL)
    private List<OfficialBoardTag> officialBoardTags = new ArrayList<>();

    public void addOfficialBoardTag(OfficialBoardTag officialBoardTag) {
        if(this.officialBoardTags == null) {
            officialBoardTags = new ArrayList<>();
        }
        officialBoardTags.add(officialBoardTag);
        officialBoardTag.setOfficialBoard(this);
    }

}
