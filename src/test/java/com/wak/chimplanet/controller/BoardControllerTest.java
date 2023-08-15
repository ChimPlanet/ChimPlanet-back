package com.wak.chimplanet.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.chimplanet.dto.responseDto.BoardResponseDto;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional(readOnly = true)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 인기게시글_조회_응답코드_확인() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/boards/popular")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString()).isNotEmpty();
    }

    @Test
    void 인기게시글_조회_한달이내_작성() throws Exception {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/boards/popular")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        List<LinkedHashMap<String, Object>> rawResults = objectMapper.readValue(response.getContentAsString(), List.class);
        List<BoardResponseDto> results = rawResults.stream()
            .map(map -> objectMapper.convertValue(map, BoardResponseDto.class))
            .collect(Collectors.toList());


        for (BoardResponseDto board : results) {
            BoardResponseDto boardResponseDto = objectMapper.convertValue(board,
                BoardResponseDto.class);
            LocalDateTime regDate = boardResponseDto.getRegDate();
            assertThat(regDate).isAfter(oneMonthAgo);
        }
    }


}