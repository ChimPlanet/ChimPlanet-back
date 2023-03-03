package com.wak.chimplanet.controller;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.service.BoardService;
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

    @GetMapping("/")
    public String testController() {
        return "test";
    }

    @GetMapping("/api/boards")
    public ResponseEntity<List<Board>> getAllBoardList() {
        return ResponseEntity.ok().body(boardService.getAllBoardList());
    }

    @GetMapping("/api/boards/{articleId}")
    public ResponseEntity<BoardDetail> getBoardOne(@PathVariable String articleId) {
        return ResponseEntity.ok().body(boardService.getBoardOne(articleId));
    }
}
