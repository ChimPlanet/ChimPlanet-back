package com.wak.chimplanet.controller;

import com.wak.chimplanet.dto.responseDto.AdminUpdateBoardResponseDto;
import com.wak.chimplanet.service.adminBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 관리자 페이지에서 게시글 수정
 * 추후 admin 기능 통합예정
 */
@RestController
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
@Slf4j
public class adminBoardController {

    private final adminBoardService adminBoardService;

    @PutMapping("/updateBoard")
    public ResponseEntity<AdminUpdateBoardResponseDto> updateBoard() {
        return ResponseEntity.ok().body(adminBoardService.updateBoard());
    }
}
