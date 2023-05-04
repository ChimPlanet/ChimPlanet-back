package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.repository.BoardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CafeBoardScheduleServiceTest {

    @Autowired
    CafeBoardScheduleService cafeBoardScheduleService;

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void saveAllBoardsPerPageTest() {
        // given

        // when
        cafeBoardScheduleService.saveAllBoardsPerPage(1);

        // then
        List<Board> savedBoards = boardRepository.findAllBoard();
        assertFalse(savedBoards.isEmpty());
    }

}