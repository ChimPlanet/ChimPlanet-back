package com.wak.chimplanet.controller;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.service.BoardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BoardService boardService;

    @ApiOperation(value = "왁물원 게시글 공고 리스트", notes = "현재 20개 고정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리스트 로드 성공", content = @Content(schema = @Schema(implementation = Board.class)))
    })
    @GetMapping("/api/boards")
    public ResponseEntity<List<Board>> getAllBoardList() {
        return ResponseEntity.ok().body(boardService.getAllBoardList());
    }

    @ApiOperation(value = "게시글 상세정보 가져오기(articleId)", notes = "왁물원 공고 게시글 내용가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공", content = @Content(schema = @Schema(implementation = BoardDetail.class)))
            // @ApiResponse(responseCode = "400", description = "접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            //@ApiResponse(responseCode = "404", description = "게시글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/boards/{articleId}")
    public ResponseEntity<BoardDetail> getBoardOne(@PathVariable String articleId) {
        return ResponseEntity.ok().body(boardService.getBoardOne(articleId));
    }
}
