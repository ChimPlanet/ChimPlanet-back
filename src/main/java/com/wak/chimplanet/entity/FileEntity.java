package com.wak.chimplanet.entity;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "이미지 아이디")
    private long fileId;

    @Column(nullable = false, unique = true, length = 1000)
    @ApiModelProperty(value = "이미지 파일명")
    private String fileName;

    @Column
    @ApiModelProperty(value = "사용 여부")
    private String useYn;

    @Column
    @ApiModelProperty(value = "연결할 url 주소")
    private String redirectUrl;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "파일 이미지 타입(Main, Mid)")
    private ImageType imageType; // 이미지 타입 [Main, Mid]

    @Column
    @ApiModelProperty(value = "모바일 사용여부")
    private String mobileYn;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @ApiModelProperty(value = "생성일자")
    private String createdDate;
}
