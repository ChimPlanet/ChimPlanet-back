package com.wak.chimplanet.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardDetailResponseDto {
    private String articleId; /* 게시글 ID */
    private String content; /* 게시글 내용 */
    private String redirectURL; /* 이동할 주소 */
    private String boardTitle; /* 게시글 제목 */
    private String writer; /* 게시글 작성자 */
    private String profileImageUrl; /* 프로필 이미지 URL */

    private Integer readCount; /* 조회수 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime regDate; /* 생성일자 */
    private String isEnd; /* 마감여부 */
    private List<BoardTagResponseDto> tags; /* 태그이름 */
    private boolean unauthorized; /* 접근권한 */

/*    public static BoardDetailResponseDto from(BoardDetail boardDetail, Board board) {
        return BoardDetailResponseDto.builder()
                .articleId(boardDetail.getArticleId())
                .content(boardDetail.getContent())
                .redirectURL(boardDetail.getRedirectURL())
                .readCount(board.getReadCount())
                .regDate(board.getRegDate())
                .isEnd(board.getIsEnd())
                .tags(board.getBoardTags().stream().map(BoardTagResponseDto::new).collect(Collectors.toList()))
                .unauthorized(board.getUnauthorized().equals("N"))
                .build();
    }*/

    public static BoardDetailResponseDto from(BoardDetail boardDetail, Board board) {
        BoardDetailResponseDtoBuilder builder = BoardDetailResponseDto.builder();
        
        if (boardDetail != null) {
            builder.articleId(boardDetail.getArticleId())
                    .content(boardDetail.getContent())
                    .redirectURL(boardDetail.getRedirectURL())
                    .readCount(boardDetail.getReadCount())
                    .writer(boardDetail.getWriter())
                    .profileImageUrl(boardDetail.getProfileImageUrl())
                    .boardTitle(board.getBoardTitle());
        }

        if (board != null) {
            builder.readCount(board.getReadCount())
                    .regDate(board.getRegDate())
                    .isEnd(board.getIsEnd())
                    .tags(board.getBoardTags().stream().map(BoardTagResponseDto::new).collect(Collectors.toList()))
                    .unauthorized(board.getUnauthorized().equals("N"));
        }

        return builder.build();
    }
    
}
