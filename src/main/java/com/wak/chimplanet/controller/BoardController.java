package com.wak.chimplanet.controller;

import com.wak.chimplanet.dto.responseDto.BoardDetailResponseDTO;
import com.wak.chimplanet.dto.responseDto.BoardResponseDto;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.service.BoardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @ApiOperation(value = "왁물원 게시글 공고 리스트", notes = "현재 20개 고정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리스트 로드 성공", content = @Content(schema = @Schema(implementation = Board.class)))
    })
    @GetMapping("/api/boards/new")
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
    public ResponseEntity<BoardDetailResponseDTO> getBoardOne(@PathVariable String articleId) {
        return ResponseEntity.ok().body(boardService.getBoardOne(articleId));
    }

    @ApiOperation(value = "데이터 베이스에서 게시글 가져오기" , notes = "1차 분류 완료")
    @PostMapping("/api/boards/old")
    public ResponseEntity<List<BoardResponseDto>> findAllBoard() {
        return ResponseEntity.ok().body(boardService.findAllBoard());
    }

    @PostMapping("/api/boards")
    @ApiOperation(value = "게시판 목록 조회 API", notes = "게시판 목록을 조회하고, 페이징 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시판 목록 조회 성공")
    })
    public ResponseEntity<Slice<BoardResponseDto>> getBoards(
        @ApiParam(value = "마지막 게시물 아이디") @RequestParam(value = "lastArticleId", required = false) String lastArticleId,
        @ApiParam(value = "한 페이지당 게시물 개수", defaultValue = "20") @RequestParam(value = "size", defaultValue = "20") int size,
        @ApiParam(value = "페이지 번호", defaultValue = "0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("articleId").descending());
        return ResponseEntity.ok().body(boardService.findBoardsByPaging(lastArticleId, pageable));
    }


    @ApiOperation(value = "데이터 베이스에서 게시글 인기글 가져오기" , notes = "정렬 기준 read_count")
    @GetMapping("/api/boards/popular")
    public ResponseEntity<List<Board>> findAllBoardByPopular() {
        return ResponseEntity.ok().body(boardService.findBoardsByReadCount());
    }

}
