package com.wak.chimplanet.service;

import com.wak.chimplanet.dto.requestDto.FileUploadRequestDto;
import com.wak.chimplanet.entity.DeviceType;
import com.wak.chimplanet.entity.FileEntity;
import com.wak.chimplanet.entity.ImageType;
import com.wak.chimplanet.repository.FileRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManagerFactory;
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

    @Transactional
    public Long updateImage(FileUploadRequestDto fileUploadRequestDto, MultipartFile[] multipartFiles) throws Exception {
        FileEntity findFileEntityById = fileRepository.findById(fileUploadRequestDto.getFileId())
            .orElseThrow(
                // ID로 조회한 결과값이 없는 경우
                () -> new IllegalArgumentException("File not found with id " + fileUploadRequestDto.getFileId())
            );
        
        // 조회한 엔티티를 변경
        FileEntity fileEntity = findFileEntityById.updateFile(fileUploadRequestDto, multipartFiles);

        log.info("updateFileEntity : {}", fileEntity.toString());

        // 이미지 파일변경
        findFileEntityById.changeFile(fileEntity.getFileName()
            , fileEntity.getFileName(), filePath, multipartFiles);

        return findFileEntityById.getFileId();
    }

    @Transactional
    public Long deleteImage(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
            .orElseThrow(() -> new IllegalArgumentException("File not found with id " + fileId));

        Path filePath = Paths.get(fileEntity.getImageUri());

        if (!Files.exists(filePath)) {
            // 삭제할 파일이 존재하지 않는경우
            throw new IllegalArgumentException("File does not exist.");
        }

        try {
            Files.delete(filePath);
            fileRepository.delete(fileEntity);
        } catch (IOException e) {
            // 파일 삭제 중 오류 발생
            throw new IllegalArgumentException("Failed to delete file.");
        }

        return fileId;
    }
}
