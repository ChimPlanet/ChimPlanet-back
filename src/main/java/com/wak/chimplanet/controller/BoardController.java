package com.wak.chimplanet.controller;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:80"})
    public List<Board> getAllBoardList() {
        return boardService.getAllBoardList();
    }
}
