package com.wak.chimplanet.controller;

import com.wak.chimplanet.entity.Board;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/tagList")
    public String getAllBoardList() {
        logger.info("tagList 조회");
        return "test";
    }
}
