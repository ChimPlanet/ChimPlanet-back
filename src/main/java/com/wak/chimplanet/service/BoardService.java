package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.naver.NaverCafeAtricleApi;
import com.wak.chimplanet.repository.BoardRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
// @RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final NaverCafeAtricleApi naverCafeAtricleApi;
    private final BoardRepository boardRepository;

    private final static String API_URL = "https://apis.naver.com/cafe-web/cafe2/ArticleListV2.json?"
        + "search.clubid=27842958"
        + "&search.queryType=lastArticle"
        + "&search.menuid=148"
        + "&search.perPage=50" // 페이징 + 갯수 처리 필요
        + "&search.page=";

    @Autowired
    public BoardService(NaverCafeAtricleApi naverCafeAtricleApi,
        BoardRepository boardRepository) {
        this.naverCafeAtricleApi = naverCafeAtricleApi;
        this.boardRepository = boardRepository;
    }

    /**
     * 최신 게시판 받아오기
     */
    public ArrayList<Board> getAllBoardList() {
        return naverCafeAtricleApi.getArticles(API_URL);
    }

    /**
     * 게시판 게시물 가져오기
     */
    public BoardDetail getBoardOne(String articleId) {
        return naverCafeAtricleApi.getNaverCafeArticleOne(articleId);
    }

    /**
     * 게시판 내용 대량 저장하기
     *  10페이지씩 순회하면서 데이터 저장
     */
    @Transactional
    public List<Board> saveAllBoards() {
        ArrayList<Board> articles = new ArrayList<>();

        for(int i = 1; i <= 20; i++) {
            articles = naverCafeAtricleApi.getArticles(API_URL + i);
            log.info("articleSize : {} ", articles.size());
            boardRepository.saveAll(articles);
        }

        return articles;
    }

    @Transactional
    public void saveBoard(Board board) {
        boardRepository.saveBoard(board);
    }

    /**
     * 게시판 목록 가져오기 from DataBase
     */
    public List<Board> findAllBoard() {
        return boardRepository.findAllBoard();
    }

    /**
     * 게시글에서 태그 리스트 분류하기
     */
    public List<String> categorizingTag(String content) {
        // 문장에서 찾은 태그명
        Set<String> foundTags = new HashSet<>();

        List<String> tags = Arrays.asList("공식", "백엔드", "기획", "프론트엔드", "디자인", "디자이너");

        for(String tag : tags) {
            if(kmpSearch(content, tag)) {
                foundTags.add(tag);
            }
        }

        log.info("찾은 태그명 : {}", foundTags.toString());
        return foundTags.stream().collect(Collectors.toList());
    }

    /**
     * 태그명 찾기 - KMP 알고리즘 사용
     */
    private boolean kmpSearch(String content, String tag) {
        int n = content.length();
        int m = tag.length();
        int[] pi = getPi(tag);

        int idx = 0; // 글자수
        for (int i = 0; i < n; i++) {
            while (idx > 0 && content.charAt(i) != tag.charAt(idx)) {
                idx = pi[idx - 1];
            }
            
            // 글자가 대응되는 경우
            if (content.charAt(i) == tag.charAt(idx)) {
                if (idx == m - 1) {
                    log.info("{} 번째에서 태그 발견 ~ {}", i-idx-1, i+1);
                    return true;
                } else {
                    idx++;
                }
            }
        }
        return false;
    }

    /**
     * tag의 접두사와 접미사가 일치하는 길의를 계산하는 배열 pi 를 리턴
     */
    private int[] getPi(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];

        int j = 0;
        for (int i = 1; i < m; i++) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                pi[i] = ++j;
            }
        }
        return pi;
    }

}
