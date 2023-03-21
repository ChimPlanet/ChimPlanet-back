package com.wak.chimplanet.entity;

import com.wak.chimplanet.dto.requestDto.FileUploadRequestDto;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="file")
@Table(name = "file", uniqueConstraints = {@UniqueConstraint(
    name = "sequence_unique",
    columnNames = {"sequence", "imageType"}
)})
@Slf4j
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
    private ImageType imageType;

    @Column
    @ApiModelProperty(value = "기기별 이미지 타입(MOBILE, PC)")
    private DeviceType deviceType;

    @Column
    @ApiModelProperty(value = "리다이렉션 타입(Y, N)")
    private String redirectType;

    @Column
    @ApiModelProperty(value = "배너 순서")
    private int sequence;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @ApiModelProperty(value = "생성일자")
    @CreatedDate
    private LocalDateTime createdDate;

    //== 비즈니스 로직 ==/
    /**
     * 이미지 파일 변경
     * @param requestDto
     */
    public FileEntity changeImage(FileUploadRequestDto requestDto, String filePath) {
        MultipartFile[] multipartFile = requestDto.getFile();
        String originFileName = multipartFile[0].getOriginalFilename();
        long fileSize = multipartFile[0].getSize();
        String safeFileName = UUID.randomUUID().toString() + "-" + originFileName; // 고유 파일명

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("images/")// 리소스 핸들러 명칭
            .path(safeFileName)
            .toUriString();

        File file = new File(filePath + File.separator + safeFileName);

        log.info("originFileName: {}, fileSize: {}, safeFileName: {}, filePath: {}",
            originFileName, fileSize, safeFileName, filePath);

        try {
            multipartFile[0].transferTo(file);
        } catch (IOException e) {
            log.error("[FileService.class] IOException", e);
            throw new RuntimeException(e);
        }

        return FileEntity.builder()
            .fileName(safeFileName)
            .deviceType(deviceType)
            .redirectUrl(redirectUrl)
            .useYn(useYn)
            .imageType(imageType)
            .imageUri(fileUri)
            .sequence(sequence)
            .build();
    }
}
