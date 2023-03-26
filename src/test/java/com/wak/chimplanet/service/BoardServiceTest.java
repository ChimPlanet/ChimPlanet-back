package com.wak.chimplanet.service;

import com.wak.chimplanet.dto.responseDto.BoardDetailResponseDTO;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.naver.NaverCafeAtricleApi;
import com.wak.chimplanet.repository.TagObjRepository;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired NaverCafeAtricleApi naverCafeAtricleApi;
    @Autowired
    TagObjRepository tagRepository;
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
        BoardDetail boardDetail = naverCafeAtricleApi.getNaverCafeArticleOne("9857025");
        String content = boardDetail.getContent();

        // when
        List<String> expectedTags = Arrays.asList("백엔드");
        List<TagObj> actualTags = boardService.categorizingTag(content);

        // then
        assertEquals(actualTags.get(0).getTagName(), "백엔드");
    }

    @Test(expected = NullPointerException.class)
    public void 권한이_없는_게시물() {
        String articleId = "10117009"; // 권한이 없는 친구
        BoardDetailResponseDTO boardOne = boardService.getBoardOne(articleId);
    }

}