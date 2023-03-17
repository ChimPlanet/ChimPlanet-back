package com.wak.chimplanet.entity;

import io.swagger.annotations.Api;
import java.time.LocalDateTime;
import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="file")
@Table(name = "file", uniqueConstraints = {@UniqueConstraint(
    name = "sequence_unique",
    columnNames = {"sequence"}
)})
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

    @Column
    @ApiModelProperty(value = "이미지파일 uri")
    private String imageUri;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "파일 이미지 타입(MAIN, MID)")
    private ImageType imageType; // 이미지 타입 [Main, Mid]

    @Column
    @ApiModelProperty(value = "기기별 이미지 타입(MOBILE, PC)")
    private DeviceType deviceType;

    @Column
    @ApiModelProperty(value = "리다이렉션 타입")
    private String redirectType;

    @Column
    @ApiModelProperty(value = "배너 순서")
    private int sequence;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @ApiModelProperty(value = "생성일자")
    @CreatedDate
    private LocalDateTime createdDate;
}
