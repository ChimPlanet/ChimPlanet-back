package com.wak.chimplanet.entity;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BoardDetail {

    private String articleId;
    private String content;
    private String redirectURL;
    private String boardTitle; /* 게시글 제목 */
    private String writer; /* 게시글 작성자 */
    private List<TagObj> tagList;

}
