package com.wak.chimplanet.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder // 임시 추가 -> DTO 로 옮겨야함
public class Board {
    @Id
    private String boardId;
    private String writer;
    private String title;
    private String articleId;
    private String likeCount;
    private String viewCount;
    private String regDate;
    private String imgUrl;
    private String endStr; // 추후 ENUM 으로 리팩토링
}
