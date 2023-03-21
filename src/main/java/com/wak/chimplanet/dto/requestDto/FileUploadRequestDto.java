package com.wak.chimplanet.dto.requestDto;

import com.wak.chimplanet.entity.DeviceType;
import com.wak.chimplanet.entity.FileEntity;
import com.wak.chimplanet.entity.ImageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadRequestDto {

    private Long fileId;
    private MultipartFile[] file;
    private ImageType imageType;
    private String useYn;
    private DeviceType deviceType;
    private String redirectUrl;
    private int sequence;


    public FileEntity toEntity() {
        return FileEntity.builder().build();
    }

}
