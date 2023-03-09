package com.wak.chimplanet.controller;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.FileEntity;
import com.wak.chimplanet.service.FileService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @ApiOperation(value = "이미지 파일 업로드")
    public ResponseEntity<FileEntity> uploadImage(HttpServletRequest request,
        @RequestPart(value = "file", required = false)MultipartFile[] files,
        @RequestParam(value="fileType", required = false) String fileType) {
        return ResponseEntity.ok().body(fileService.uploadImage(files, fileType));
    }

}
