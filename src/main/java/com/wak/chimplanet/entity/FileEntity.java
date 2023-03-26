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
/*@Table(name = "file", uniqueConstraints = {@UniqueConstraint(
    name = "sequence_unique",
    columnNames = {"sequence", "imageType"}
)})*/
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
     * 객체 생성
     */
    public static FileEntity createFileEntity(String fileName, String useYn, String redirectUrl, String imageUri,
                                    ImageType imageType, DeviceType deviceType, String redirectType, int sequence) {
        return FileEntity.builder()
                .fileName(fileName)
                .useYn(useYn)
                .redirectUrl(redirectUrl)
                .imageUri(imageUri)
                .imageType(imageType)
                .deviceType(deviceType)
                .redirectType(redirectType)
                .sequence(sequence)
                .build();
    }

    /**
     * DDD 패턴으로 리팩토링
     * 이미지 파일 변경
     */
    public void changeFile(String originalFileName, String safeFileName, String filePath, MultipartFile[] multipartFile) {
        File file = new File(filePath + File.separator + safeFileName);
        File originalFile = new File(filePath + File.separator + originalFileName);

        log.info("save FilePath: {}", filePath + File.separator + safeFileName);

        try {
            // 기존파일 삭제
            if (originalFile.exists()) {
                boolean result = originalFile.delete();
                if (!result) {
                    log.error("Failed to delete original file: {}", originalFile.getAbsolutePath());
                }
            }
            multipartFile[0].transferTo(file);
        } catch (IOException e) {
            log.error("[FileService.class] IOException", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 이미지파일 정보 변경 감지 update
     */
    public FileEntity updateFile(FileUploadRequestDto fileUploadRequestDto, MultipartFile[] multipartFile) {
        String safeFileName = UUID.randomUUID().toString() + "-" + multipartFile[0].getOriginalFilename();

        log.info("Updating file with id {} with the following changes: fileName={}, useYn={}, deviceType={}, imageType={}, redirectUrl={}, imageUri={}, redirectType={}, sequence={}",
            this.fileId, safeFileName, fileUploadRequestDto.getUseYn(), fileUploadRequestDto.getDeviceType(),
            fileUploadRequestDto.getImageType(), fileUploadRequestDto.getRedirectUrl(), getImageUri(safeFileName),
            fileUploadRequestDto.getRedirectType(), fileUploadRequestDto.getSequence());

        this.fileName = safeFileName;
        this.useYn = fileUploadRequestDto.getUseYn();
        this.deviceType = fileUploadRequestDto.getDeviceType();
        this.imageType = fileUploadRequestDto.getImageType();
        this.redirectUrl = fileUploadRequestDto.getRedirectUrl();
        this.imageUri = getImageUri(safeFileName);
        this.redirectType = fileUploadRequestDto.getRedirectType();
        this.sequence = fileUploadRequestDto.getSequence();

        return this;
    }

    /**
     * 시퀀스 값을 변경합니다.
     * @param sequence 변경할 시퀀스 값
     */
    public void changeSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * 고유 파일명 생성하여 리턴
     * @param multipartFile
     * @return{String}
     */
    public String getFileName(MultipartFile multipartFile) {
        String originFileName = multipartFile.getOriginalFilename();
        return UUID.randomUUID().toString() + "-" + originFileName; // 고유 파일명
    }

    /**
     * 호출할 이미지 URI 생성하여 리턴
     * @return{String}
     */
    public String getImageUri(String safeFileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("images/")// 리소스 핸들러 명칭
            .path(safeFileName)
            .toUriString();
    }
}
