package com.wak.chimplanet.controller;

import com.wak.chimplanet.entity.OfficialBoard;
import com.wak.chimplanet.service.OfficialBoardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor //생성자 주입
@RequestMapping("/api/officialBoard")
public class OfficialBoardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OfficialBoardService officialBoardService;

    @ApiOperation(value = "왁물원 공식 공고 리스트")
    @GetMapping("")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "리스트 로드 성공", content = @Content(schema = @Schema(implementation = OfficialBoard.class)))
    })
    public ResponseEntity<List<OfficialBoard>> getAllOfficialBoard(){
        return ResponseEntity.ok().body(officialBoardService.getAllOfficialBoard());
    }

    @ApiOperation(value = "왁물원 공식 공고 Data 저장")
    @PostMapping(value = "/")
    public ResponseEntity<List<OfficialBoard>> saveOfficialBoard(OfficialBoard officialBoard) {
        return ResponseEntity.ok().body(officialBoardService.saveOfficialBoard(officialBoard));
    }

    @DeleteMapping(value = "/{articleId}")
    public ResponseEntity<List<OfficialBoard>> deleteOfficialBoard(@PathVariable String articleId) {

        return ResponseEntity.ok().body(officialBoardService.deleteOfficialBoard(articleId));
    }

}
