package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.OfficialBoard;
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
public class OfficialBoardServiceTest {
    @Autowired
    OfficialBoardService officialBoardService;

    @Test
    public void 공식_게시글_조회(){

        OfficialBoard officialBoard = new OfficialBoard().builder()
                .build();

        List<OfficialBoard> officialBoardList = officialBoardService.getAllOfficialBoard();

        System.out.println("=======length :  "  + officialBoardList.size() + "======" );

        int listIndex = 0;
        for (OfficialBoard obj : officialBoardList) {

            System.out.println("================");
            System.out.println(obj.toString());

            listIndex++;
        }

        assertEquals(officialBoardList.size(), listIndex);

    }

    @Test
    public void 공식_게시글_저장(){


        //임시 게시글 번호
        String articleId = "10930730";


        OfficialBoard officialBoard = new OfficialBoard().builder()
                .articleId(articleId)
                .build();

        //기존 길이
        int boardListSize = officialBoardService.getAllOfficialBoard().size();

        List<OfficialBoard> officialBoardList = officialBoardService.saveOfficialBoard(officialBoard);

        for (OfficialBoard board: officialBoardList) {
            System.out.println("articleId :: " + articleId + "저장 후 조회");
            System.out.println(board.toString());
        }

        System.out.println();

        assertEquals(boardListSize + 1, officialBoardList.size());
    }
}