package com.wak.chimplanet.controller;

import com.wak.chimplanet.dto.requestDto.FileSequenceRequestDto;
import com.wak.chimplanet.dto.requestDto.FileUploadRequestDto;
import com.wak.chimplanet.dto.responseDto.FileResponseDto;
import com.wak.chimplanet.entity.DeviceType;
import com.wak.chimplanet.entity.FileEntity;
import com.wak.chimplanet.entity.ImageType;
import com.wak.chimplanet.service.FileService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
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
    @ApiOperation(value = "배너 파일 불러오기")
    @GetMapping("banner")
    public ResponseEntity<List<FileEntity>> findAllImages() {
        return ResponseEntity.ok().body(fileService.findAllImages());
    }

    /**
     * 이미지 업로드
     */
    @PostMapping("/image/")
    @ApiOperation(value = "이미지 파일 업로드")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "files", required = false, dataType = "MultipartFile", value = "첨부 이미지 파일"),
        @ApiImplicitParam(name = "fileType", required = false, dataType = "ImageType", value = "이미지 파일 타입 선택 [MAIN, MID]"),
        @ApiImplicitParam(name = "useYn", required = false, dataType = "String", value = "이미지 파일 사용 여부 [Y, N]"),
        @ApiImplicitParam(name = "redirectUrl", required = false, dataType = "String", value = "연결될 URL 주소"),
        @ApiImplicitParam(name = "deviceType ", required = false, dataType = "DeviceType", value = "파일 사용 기기[PC, MOBILE]")
    })
    public ResponseEntity<FileEntity> uploadImage(HttpServletRequest request,
        @RequestPart(value = "file")MultipartFile[] files,
        @RequestParam(value = "fileType") ImageType ImageType,
        @RequestParam(value = "useYn") String useYn,
        @RequestParam(value = "deviceType") DeviceType deviceType,
        @RequestParam(value = "redirectUrl") String redirectUrl,
        @RequestParam(value = "redirectType") String redirectType,
        @RequestParam(value = "sequence") int sequence) {
        return ResponseEntity.ok().body(fileService.uploadImage(files, ImageType, useYn, deviceType, redirectUrl, sequence, redirectType));
    }

    /**
     * 기존이미지 변경
     * DTO -> Swagger 테스트 불가
     * @ReqestParam 으로 각각의 DTO내용을 받은 후, 새로운 DTO객체에 넣어 DTO 를 만들어 사용
     * 추후 RequestBody + RequestPart로 변경
     */
    @ApiOperation(value = "이미지 업데이트", notes = "배너 이미지 관련 업데이트")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = FileResponseDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = FileResponseDto.class)
    })
    @PutMapping("/updateImage/{fileId}")
    public ResponseEntity<FileResponseDto> updateImage(
        // @RequestPart(value= "fileUploadRequestDto") @ApiParam(value="배너 변경 정보", required = true) FileUploadRequestDto fileUploadRequestDto,
        @RequestParam(value = "fileType") ImageType ImageType,
        @RequestParam(value = "useYn") String useYn,
        @RequestParam(value = "deviceType") DeviceType deviceType,
        @RequestParam(value = "redirectUrl") String redirectUrl,
        @RequestParam(value = "sequence") int sequence,
        @RequestParam(value = "redirectType") String redirectType,
        @RequestPart(value = "files") @ApiParam(value="변경 파일", required = false) MultipartFile[] files,
        @PathVariable Long fileId) {

        FileUploadRequestDto fileUploadRequestDto = FileUploadRequestDto.createFileUploadRequestDto(
            fileId, files, ImageType, useYn, deviceType, redirectUrl, redirectType, sequence);

        try {
            // 파일 업로드 로직 수행
            fileService.updateImage(fileUploadRequestDto, files);

            // 응답 및 메타데이터 생성 부 클래스 따로 빼서 처리하기
            FileResponseDto fileResponseDto = new FileResponseDto();
            fileResponseDto.setMessage("File uploaded successfully");
            fileResponseDto.setStatus(HttpStatus.OK);

            // 메타데이터 생성
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("fileName", files[0].getOriginalFilename());
            metadata.put("fileSize", files[0].getSize());
            metadata.put("fileExtension", FilenameUtils.getExtension(files[0].getOriginalFilename()));
            metadata.put("uploadTime", LocalDateTime.now());
            fileResponseDto.setData(metadata);

            return ResponseEntity.ok().body(fileResponseDto);
        } catch (Exception e) {
            FileResponseDto fileResponseDto = new FileResponseDto();
            fileResponseDto.setMessage("File upload failed: " + e.getMessage());
            fileResponseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            fileResponseDto.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fileResponseDto);
        }
    }
    /**
     * 파일 삭제 API
     * @param fileId 삭제할 파일 ID
     * @return 삭제된 파일 ID
     * @throws IllegalArgumentException 파일이 존재하지 않거나 삭제 중 오류 발생시 예외 발생
     */
    @ApiOperation(value = "파일 삭제", notes = "파일 ID를 이용하여 파일을 삭제합니다.")
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<FileResponseDto<FileEntity>> deleteFile(@PathVariable Long fileId) {
        try {
            Long deletedFileId = fileService.deleteImage(fileId);

            FileResponseDto<FileEntity> fileResponseDto = new FileResponseDto<>();
            fileResponseDto.setStatus(HttpStatus.OK);
            fileResponseDto.setMessage("File Delete successfully");
            fileResponseDto.setData("deleteFileId :: " + deletedFileId);

            return ResponseEntity.ok(fileResponseDto);
        } catch (IllegalArgumentException e) {

            FileResponseDto<FileEntity> fileResponseDto = new FileResponseDto<>();
            fileResponseDto.setMessage("File Delete failed: " + e.getMessage());
            fileResponseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            fileResponseDto.setData("deleteFileId :: " + fileId);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fileResponseDto);
        }
    }

    @ApiOperation(value = "이미지 시퀀스를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = FileResponseDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = FileResponseDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = FileResponseDto.class)})
    @PutMapping("/sequence")
    public ResponseEntity<FileResponseDto> updateSequence(@RequestBody List<FileSequenceRequestDto> sequenceList) {
        fileService.updateSequence(sequenceList);
        return ResponseEntity.ok().body(new FileResponseDto<>());
    }

}
