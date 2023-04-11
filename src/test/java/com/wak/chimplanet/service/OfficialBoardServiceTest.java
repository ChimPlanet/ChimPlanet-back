package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.OfficialBoard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OfficialBoardServiceTest {
    @Autowired
    OfficialBoardService officialBoardService;

    @Test
    public void 공식_게시글_저장(){

        OfficialBoard officialBoard = new OfficialBoard().builder()
                .articleId("10258505")
                .build();

        officialBoardService.saveOfficialBoard(officialBoard);
    }
}