package com.wak.chimplanet.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.entity.TagObj;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardDetailResponseDTO {
    private String articleId; /* 게시글 ID */
    private String content; /* 게시글 내용 */
    private String redirectURL; /* 이동할 주소 */
    private String boardTitle; /* 게시글 제목 */
    private String writer; /* 게시글 작성자 */

    private String readCount; /* 조회수 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime regDate; /* 생성일자 */
    private String isEnd; /* 마감여부 */
    private List<TagObj> tags; /* 태그이름 */
    private boolean unauthorized; /* 접근권한 */

    public static BoardDetailResponseDTO from(BoardDetail boardDetail, Board board) {
        return BoardDetailResponseDTO.builder()
                .articleId(boardDetail.getArticleId())
                .content(boardDetail.getContent())
                .redirectURL(boardDetail.getRedirectURL())
                .readCount(board.getReadCount())
//                .regDate(LocalDateTime.parse(board.getRegDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEnd(board.getIsEnd())
                .tags(boardDetail.getTagList())
                .unauthorized(board.getUnauthorized().equals("N"))
                .build();
    }

}
