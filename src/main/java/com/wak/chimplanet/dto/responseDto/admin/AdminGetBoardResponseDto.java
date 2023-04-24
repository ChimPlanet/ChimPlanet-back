package com.wak.chimplanet.dto.responseDto.admin;


import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardTag;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminGetBoardResponseDto {

    private String articleId;
    private String boardTitle;
    private String writer;
    private LocalDateTime regDate;
    private String thumbnailURL;
    private String redirectURL;
    private Integer readCount;
    private String isEnd;
    private String unauthorized;
    private List<BoardTag> boardTags;

    /* Entity -> DTO */
    public AdminGetBoardResponseDto(Board board) {
        this.articleId = board.getArticleId();
        this.boardTitle = board.getBoardTitle();
        this.writer = board.getWriter();
        this.regDate = board.getRegDate();
        this.thumbnailURL = board.getThumbnailURL();
        this.redirectURL = board.getRedirectURL();
        this.readCount = board.getReadCount();
        this.isEnd = board.getIsEnd();
        this.unauthorized = board.getUnauthorized();
        this.boardTags = board.getBoardTags().stream()
                .collect(Collectors.toList());
    }
}