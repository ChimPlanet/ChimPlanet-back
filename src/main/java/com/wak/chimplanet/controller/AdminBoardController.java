package com.wak.chimplanet.controller;

import com.wak.chimplanet.dto.requestDto.admin.AdminBoardUpdateRequestDto;
import com.wak.chimplanet.dto.responseDto.admin.AdminBoardResponseDto;
import com.wak.chimplanet.dto.responseDto.admin.AdminGetBoardResponseDto;
import com.wak.chimplanet.dto.responseDto.admin.AdminUpdateBoardResponseDto;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.service.AdminBoardService;
import com.wak.chimplanet.service.BoardService;
import com.wak.chimplanet.service.CafeBoardScheduleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * 관리자 페이지에서 게시글 수정
 * 추후 admin 기능 통합예정
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminBoardController {

    private final AdminBoardService adminBoardService;
    private final BoardService boardService;

    @GetMapping("/board/{articleId}")
    public ResponseEntity<AdminGetBoardResponseDto> getBoard(@PathVariable String articleId) {
        return ResponseEntity.ok().body(null);
    }

    @ApiOperation(value = "관리자 게시글 수정", notes = "마감여부 및 태그 수정")
    @PutMapping("/board/updateBoard/")
    public ResponseEntity<AdminUpdateBoardResponseDto> updateBoard(
            @RequestBody AdminBoardUpdateRequestDto adminBoardUpdateRequestDto) {

        Board board =  adminBoardService.updateBoard(adminBoardUpdateRequestDto);
        return ResponseEntity.ok().body(
                new AdminUpdateBoardResponseDto("Success", HttpStatus.OK, AdminBoardResponseDto.from(board)));
    }

    private final CafeBoardScheduleService cafeBoardScheduleService;

    /**
     * 스케줄러 강제 실행을 위한 메서드
     */
    @ApiOperation(value = "스케줄러 강제 실행 API", notes = "수집할 페이지 갯수를 입력해주세요 기본값 : 20")
    @GetMapping("/scheduler/{pageSize}")
    public void scheduleStart(@RequestParam(required = false) Optional<Integer> inputPageSize) {
        log.info("scheduleNaverCafeBoard task exec");
        // List<Board> boardList = boardService.saveAllBoards();
        cafeBoardScheduleService.saveAllBoardsPerPage(inputPageSize.orElse(20));
    }
}
