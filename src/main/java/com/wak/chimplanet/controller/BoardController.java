package com.wak.chimplanet.controller;

import com.wak.chimplanet.common.config.exception.ApiErrorResult;
import com.wak.chimplanet.common.config.exception.UnauthorizedException;
import com.wak.chimplanet.dto.responseDto.BoardDetailResponseDto;
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
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = BoardDetail.class)))
            , @ApiResponse(responseCode = "401", description = "권한이 없는 게시글입니다.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/boards/{articleId}")
    public ResponseEntity<?> getBoardOne(@PathVariable String articleId) {
        try {
            BoardDetailResponseDto result = boardService.getBoardOne(articleId);
            return ResponseEntity.ok().body(result);
        } catch (UnauthorizedException e) {
            ApiErrorResult errorResult = new ApiErrorResult(HttpStatus.UNAUTHORIZED.value(), "권한이 없는 게시글입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResult);
        }
    }

    @ApiOperation(value = "데이터 베이스에서 게시글 가져오기" , notes = "1차 분류 완료")
    @PostMapping("/api/boards/old")
    public ResponseEntity<List<BoardResponseDto>> findAllBoard() {
        return ResponseEntity.ok().body(boardService.findAllBoard());
    }

    @PostMapping("/api/boards")
    @ApiOperation(value = "게시판 목록 조회 API", notes = "게시판 목록을 조회하고, 페이징 처리합니다. 기본 정렬조건은 게시글 ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시판 목록 조회 성공")
    })
    public ResponseEntity<Slice<BoardResponseDto>> getBoards(
        @ApiParam(value = "마지막 게시물 아이디") @RequestParam(value = "lastArticleId", required = false) String lastArticleId,
        @ApiParam(value = "한 페이지당 게시물 개수", defaultValue = "20") @RequestParam(value = "size", defaultValue = "20") int size,
        @ApiParam(value = "페이지 번호", defaultValue = "0") @RequestParam(value = "page", defaultValue = "0") int page,
        @ApiParam(value = "정렬 컬럼", defaultValue = "articleId") @RequestParam(value = "sort", defaultValue = "articleId") String sort,
        @ApiParam(value = "마감 여부[END, ING]") @RequestParam(value = "isEnd", required = false) String isEnd
    ) {
        // 기본은 Descending
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return ResponseEntity.ok().body(boardService.findBoardsByPaging(lastArticleId, pageable, isEnd));
    }


    @GetMapping("/api/boards/recruitBoard")
    public ResponseEntity<Map<String, Object>> getRecruitBoardCount() {
        return ResponseEntity.ok().body(boardService.getRecruitBoardCount());
    }

    @ApiOperation(value = "태그명으로 검색하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/api/boards/search")
    public ResponseEntity<List<BoardResponseDto>> searchBoard(
        @ApiParam(value = "게시글 제목", required = false) @RequestParam(value = "title", required = false) String title,
        @ApiParam(value = "검색하려는 태그", required = false) @RequestParam(required = false) List<Long> searchTagId) {
        try {
            log.info("parameter title : {} , tagsId : {}", title, searchTagId.toString());
            List<BoardResponseDto> findBoardList = boardService.findBoardByTagIds(searchTagId, title);
            return ResponseEntity.ok().body(findBoardList);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 에러 메시지를 전달할 수 있도록 수정
        }
    }

}
