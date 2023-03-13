package com.wak.chimplanet.service;

import static org.junit.Assert.*;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired BoardService boardService;
    @Autowired EntityManager em;

    /**
     * 네이버 게시판에서 데이터 가져와서 저장
     */
    @Test
    @Rollback(value = false)
    @Disabled
    public void 게시판_글목록_저장_및_확인() {
        List<Board> insertList = boardService.saveAllBoards();
        em.flush();
        // assertArrayEquals(insertList.toArray(new Board[0]), boardService.findAllBoard().toArray(new Board[0]));
    }

    @Test
    public void 태그명찾기_테스트() {
        //given
        BoardDetail boardDetail = boardService.getBoardOne("9857025");
        String content = boardDetail.getContent();

        System.out.println(content);

        // when
        List<String> expectedTags = Arrays.asList("백엔드, 디자이너, 기획, 디자인");
        List<String> actualTags = boardService.categorizingTag(content);

        System.out.println("actualTags = " + actualTags.toString());

        // then
        assertArrayEquals(expectedTags.toArray(new String[0]), actualTags.toArray(new String[0]));
    }

}