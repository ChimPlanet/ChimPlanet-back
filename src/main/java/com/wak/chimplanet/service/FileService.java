package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.DeviceType;
import com.wak.chimplanet.entity.FileEntity;
import com.wak.chimplanet.entity.ImageType;
import com.wak.chimplanet.repository.FileRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    @Value("${path.upload-images}")
    private String filePath;

    /**
     * 이미지 전체 조회하기
     */
    public List<FileEntity> findAllImages() {
        return fileRepository.findAll();
    }

    /**
     * 이미지 업로드
     * 추후 DTO 로 리팩토링 변경예정
     */
    public FileEntity uploadImage(MultipartFile[] files, ImageType imageType, String useYn,
        DeviceType deviceType, String redirectUrl, int sequence) {
        String fileNames = "";

        String originFileName = files[0].getOriginalFilename();
        long fileSize = files[0].getSize();
        String safeFile = UUID.randomUUID().toString() + "-" + originFileName; // 고유 파일명

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("images/")// 리소스 핸들러 명칭
            .path(safeFile)
            .toUriString();

        File file = new File(filePath + File.separator + safeFile);

        log.info("originFileName: {}, fileSize: {}, safeFileName: {}, filePath: {}",
            originFileName, fileSize, safeFile, filePath);

        try {
            files[0].transferTo(file);
        } catch (IOException e) {
            log.error("[FileService.class] IOException", e);
            throw new RuntimeException(e);
        }

        final FileEntity fileEntity = FileEntity.builder()
            .fileName(safeFile)
            .deviceType(deviceType)
            .redirectUrl(redirectUrl)
            .useYn(useYn)
            .imageType(imageType)
            .imageUri(fileUri)
            .sequence(sequence)
            .build();

        return fileRepository.save(fileEntity);
    }

    public FileEntity updateImage(Long fileId, MultipartFile file) {
        /**
         * 구현예정
         */
        return null;
    }
}
