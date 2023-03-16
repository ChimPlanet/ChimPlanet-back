package com.wak.chimplanet.entity;

import java.util.List;
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

    @Id
    private String articleId;
    private String content;
    private String redirectURL;
    private List<TagObj> tagList;

}
