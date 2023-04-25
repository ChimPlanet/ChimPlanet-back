package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.naver.NaverCafeArticleApi;
import com.wak.chimplanet.repository.TagObjRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
                    List<TagObj> tagObjs = categorizingTag(content, tags);
                }
            }

        }
    }

    /**
     * 게시글에서 태그 리스트 분류하기
     */
    public List<TagObj> categorizingTag(String content, List<TagObj> tags) {
        if(content.isEmpty()) return null;

        // 문장에서 찾은 태그명
        Set<String> foundTags = new HashSet<>();

        // 문장에서 찾은 태그 코드
        Set<TagObj> findTagSet = new HashSet<>();

        for(TagObj tag : tags) {
            if(content.contains(tag.getTagName())) {
                foundTags.add(tag.getTagName());
                findTagSet.add(tag);
            }
        }

        log.info("찾은 태그명 : {}", foundTags.toString());

        return new ArrayList<>(findTagSet);
    }


}
