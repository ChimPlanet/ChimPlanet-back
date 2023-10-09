package com.wak.chimplanet.dto.responseDto;

import com.wak.chimplanet.entity.Board;
import java.util.List;
import java.util.stream.Collectors;

import lombok.*;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 게시글 목록을 리턴할 Response 클래스
 * Entity 클래스를 생성자 파라미터로 받아 DTO 로 변환하여 응답합니다.
 * Board 엔티티를 그대로 반환하는 경우 Board 와 BoardTag 의 무한참조를 방지하기 위해
 * 별도의 responseDto 객체를 생성합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {

    private String articleId;
    private String boardTitle;
    private String writer;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime regDate;
    private String thumbnailURL;
    private String redirectURL;
    private Integer readCount;
    private String isEnd;
    private String unauthorized;
    private String teamOperationInfo;
    private List<BoardTagResponseDto> boardTags;

    /* Entity -> Dto */
    public BoardResponseDto(Board board) {
        this.articleId = board.getArticleId();
        this.boardTitle = board.getBoardTitle();
        this.writer = board.getWriter();
        this.regDate = board.getRegDate();
        this.thumbnailURL = board.getThumbnailURL();
        this.redirectURL = board.getRedirectURL();
        this.readCount = board.getReadCount();
        this.isEnd = board.getIsEnd();
        this.unauthorized = board.getUnauthorized();
        this.teamOperationInfo = board.getTeamOperationInfo();
        this.boardTags = board.getBoardTags().stream().map(BoardTagResponseDto::new).collect(
            Collectors.toList());
    }

    /**
     * 리스트 형태로 전달
     */
    public static List<BoardResponseDto> from(List<Board> boards) {
        return boards.stream()
            .map(BoardResponseDto::new)
            .collect(Collectors.toList());
    }
}
