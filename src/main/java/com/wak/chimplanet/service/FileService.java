package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.FileEntity;
import com.wak.chimplanet.repository.FileRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;

    private final String filePath = "src/main/resources/static/images/";
    
    public List<FileEntity> findAllImages() {
        return fileRepository.findAll();
    }

    /**
     * 이미지 업로드
     * @return Map<String, Object>
     */
    public FileEntity uploadImage(MultipartFile[] files, String comment) {
        String fileNames = "";

        String originFileName = files[0].getOriginalFilename();
        long fileSize = files[0].getSize();
        String safeFile = System.currentTimeMillis() + originFileName; // 고유 파일명
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
            .filename(safeFile)
            .build();

        return fileRepository.save(fileEntity);
    }
}
