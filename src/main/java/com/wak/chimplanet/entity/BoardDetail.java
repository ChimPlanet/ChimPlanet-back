package com.wak.chimplanet.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BoardDetail {

    @Id
    private String articleId;
    private String content;
    private String redirectURL;
    // private List<Tag> tagList; -> 태그 개발 필요

}
