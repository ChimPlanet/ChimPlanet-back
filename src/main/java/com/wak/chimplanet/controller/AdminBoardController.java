package com.wak.chimplanet.controller;

import com.wak.chimplanet.dto.responseDto.admin.AdminGetBoardResponseDto;
import com.wak.chimplanet.dto.responseDto.admin.AdminUpdateBoardResponseDto;
import com.wak.chimplanet.entity.BoardTag;
import com.wak.chimplanet.service.adminBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 관리자 페이지에서 게시글 수정
 * 추후 admin 기능 통합예정
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminBoardController {

    private final adminBoardService adminBoardService;

    @GetMapping("/board/{articleId}")
    public ResponseEntity<AdminUpdateBoardResponseDto> getBoard(@PathVariable String articleId) {
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/board/updateBoard/{articleId}}")
    public ResponseEntity<AdminGetBoardResponseDto> updateBoard(
            @PathVariable String articleId,
            @RequestParam List<BoardTag> boardTags,
            @RequestParam String isEnd
    ) {
        return ResponseEntity.ok().body(adminBoardService.updateBoard());
    }
}
