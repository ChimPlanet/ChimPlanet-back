package com.wak.chimplanet.dto.responseDto;

import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class BoardResponseDto {

    private Long boardId;
    private String writer;
    private String title;
    private Long articleId;
    private String likeCount;
    private String viewCount;
    private String regDate;
}
