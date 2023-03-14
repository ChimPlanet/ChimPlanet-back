package com.wak.chimplanet.controller;

import com.wak.chimplanet.entity.FileEntity;
import com.wak.chimplanet.service.FileService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 배너 이미지 업로드 처리
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
@Slf4j
public class FileController {

    private final FileService fileService;

    /**
     * 이미지 불러오기
     */
    @GetMapping("banner")
    public ResponseEntity<List<FileEntity>> findAllImages() {
        return ResponseEntity.ok().body(fileService.findAllImages());
    }

    /**
     * 이미지 업로드
     * @return Map<String, Object>
     */
    @PostMapping("/image")
    public ResponseEntity<FileEntity> uploadImage(HttpServletRequest request,
        @RequestParam(value = "file", required = false)MultipartFile[] files,
        @RequestParam(value="comment", required = false) String comment) {
        return ResponseEntity.ok().body(fileService.uploadImage(files, comment));
    }

}
