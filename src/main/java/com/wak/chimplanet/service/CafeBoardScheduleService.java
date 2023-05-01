package com.wak.chimplanet.service;

import com.wak.chimplanet.common.util.Utility;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.entity.BoardTag;
import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.naver.NaverCafeArticleApi;
import com.wak.chimplanet.repository.BoardRepository;
import com.wak.chimplanet.repository.TagObjRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.swing.text.html.HTML.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CafeBoardScheduleService
 * 왁물원 인력관리 게시글 Schedule 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CafeBoardScheduleService {

    private final NaverCafeArticleApi naverCafeArticleApi;
    private final TagObjRepository tagObjRepository;
    private final BoardRepository boardRepository;

    private final static String API_URL = "https://apis.naver.com/cafe-web/cafe2/ArticleListV2.json?"
        + "search.clubid=27842958" // 왁물원 카페 ID
        + "&search.queryType=lastArticle" // 최신글 부터
        + "&search.menuid=148" // 인력관리 사무소 MENU ID
        + "&search.perPage=20" // 페이지 별로 몇개씩 게시글
        + "&search.page="; // 특정 페이지 이동

    @Transactional
    public void saveAllBoardsPerPage() {
        ArrayList<Board> boards = new ArrayList<>();
        List<TagObj> tags = tagObjRepository.findAll();

        int pageSize = 10; /*  저장할 페이지 갯수 */

        Instant startTime = Instant.now(); // 시작 시간

        for(int i = 0; i <= pageSize; i++) {
            ArrayList<Board> articles = naverCafeArticleApi.getArticles(API_URL + i);

            log.info("articleSize : {} ", articles.size());

            for(Board board : articles) {
                String articleId = board.getArticleId();

                BoardDetail boardDetail = naverCafeArticleApi.getNaverCafeArticleOne(articleId);

                // 접근 권한이 없는 경우 Y 처리
                if(boardDetail == null) {
                    board.setUnauthorized("Y");
                } else {
                    String content = Optional.ofNullable(boardDetail.getContent()).orElse(null);
                    List<TagObj> tagObjs = Utility.categorizingTag(content, tags);
                    List<BoardTag> boardTags = new ArrayList<>();

                    for(TagObj tag : tags) {
                        BoardTag boardTag = BoardTag.createBoardTag(tag, board);
                        board.addBoardTag(boardTag); // Board의 연관관계 메서드로 BoardTag 추가
                        boardTags.add(boardTag); // BoardTag 리스트에도 추가
                    }

                    board.updateBoard(board, boardTags);
                }

                boards.add(board);
            }

            boardRepository.saveAll(boards);
        }

        Instant endTime = Instant.now(); // 종료 시간
        long elapsedTime = Duration.between(startTime, endTime).toMillis(); // 실행 시간
        int savedBoardCount = boards.size(); // 저장한 게시글 수

        log.info("saveAllBoardsPerPage task finished. elapsedTime(ms)={}, savedBoardCount={}", elapsedTime, savedBoardCount);
    }


}
