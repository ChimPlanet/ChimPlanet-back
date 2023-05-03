package com.wak.chimplanet.service;

import static org.junit.Assert.assertEquals;
import static com.wak.chimplanet.entity.QBoard.board;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wak.chimplanet.common.util.Utility;
import com.wak.chimplanet.dto.responseDto.BoardResponseDto;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;

import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.naver.NaverCafeArticleApi;
import com.wak.chimplanet.repository.BoardRepository;
import com.wak.chimplanet.repository.TagObjRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;



@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    NaverCafeArticleApi naverCafeArticleApi;
    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired EntityManager em;
    @Autowired TagObjRepository tagObjRepository;



    /**
     * 네이버 게시판에서 데이터 가져와서 저장
     */
    @Test
    @Rollback(value = false)
    @Disabled
    public void 게시판_글목록_저장_및_확인() {
        // List<Board> insertList = boardService.saveAllBoards();
        // assertArrayEquals(insertList.toArray(new Board[0]), boardService.findAllBoard().toArray(new Board[0]));
    }

    @Test
    public void 태그명찾기_테스트() {
        //given
        BoardDetail boardDetail = naverCafeArticleApi.getNaverCafeArticleOne("9857025");
        String content = boardDetail.getContent();
        List<TagObj> tags = tagObjRepository.findAll();

        // when
        List<String> expectedTags = Arrays.asList("백엔드");
        List<TagObj> actualTags = Utility.categorizingTag(content, tags);

        // then
        assertEquals(actualTags.get(0).getTagName(), "백엔드");
    }

    @Test
    public void 권한이_없는_게시물() {
        // given
        String articleId = "10117009"; // 권한이 없는 게시물 ID
        Board board = Board.builder()
            .articleId(articleId)
            .unauthorized("N")
            .build();

        boardRepository.saveBoard(board);

        // when
        Board retrievedBoard = boardRepository.findById(articleId).orElse(null);
        if(naverCafeArticleApi.getNaverCafeArticleOne(articleId) == null) {
            retrievedBoard.setUnauthorized("Y");
            boardRepository.saveBoard(retrievedBoard);
        }

        // then
        assertEquals("Y", boardRepository.findById(articleId).orElse(null).getUnauthorized());
    }

    @Test
    public void findByQueryDsl() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        Board findBoard = queryFactory
            .select(board)
            .from(board)
            .where(board.articleId.eq("10694851"))
            .fetchOne();

        assertEquals("10694851", findBoard.getArticleId());
    }

    @Test
    public void findBoardsByPaging() {
        final int expectResultSize = 20;

        Pageable pageable = PageRequest.of(0, 20, Sort.by("articleId").descending());
        Slice<BoardResponseDto> end = boardService.findBoardsByPaging(null, pageable, "END");

        int resultSize = end.getSize();

        assertEquals(expectResultSize, resultSize);
    }

    @Test
    public void findBoardByTagIds_withTagIdsAndTitle_shouldReturnSliceOfBoards() {
        // Given
        List<String> tagIds = Arrays.asList("101");
        String title = "개발";
        String lastArticleId = null;

        Pageable pageable = PageRequest.of(0, 20, Sort.by("articleId").descending());
        // When
        List<BoardResponseDto> result = boardService.findBoardByTagIds(tagIds, title);

        // Then
        assertEquals(result.get(0).getBoardTitle().contains("개발"), true);
    }
}