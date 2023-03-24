package com.wak.chimplanet.dto.requestDto;

import com.wak.chimplanet.entity.DeviceType;
import com.wak.chimplanet.entity.ImageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadRequestDto {

    private Long fileId;
    private MultipartFile[] file;
    private ImageType imageType;
    private String useYn;
    private DeviceType deviceType;
    private String redirectUrl;
    private String redirectType;
    private int sequence;


    public static FileUploadRequestDto createFileUploadRequestDto(Long fileId, MultipartFile[] file, ImageType imageType,
        String useYn, DeviceType deviceType, String redirectUrl, String redirectType, int sequence) {
        return FileUploadRequestDto.builder()
            .fileId(fileId)
            .file(file)
            .imageType(imageType)
            .useYn(useYn)
            .deviceType(deviceType)
            .redirectUrl(redirectUrl)
            .redirectType(redirectType)
            .sequence(sequence)
            .build();
    }
}
