package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.naver.NaverCafeAtricleApi;
import com.wak.chimplanet.repository.BoardRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
// @RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final NaverCafeAtricleApi naverCafeAtricleApi;
    private final BoardRepository boardRepository;

    private final static String API_URL = "https://apis.naver.com/cafe-web/cafe2/ArticleListV2.json?"
        + "search.clubid=27842958"
        + "&search.queryType=lastArticle"
        + "&search.menuid=148"
        + "&search.page=1&search.perPage=20"; // 페이징 + 갯수 처리 필요

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
        // ArrayList<Board> articles = naverCafeAtricleApi.getArticles(API_URL);
        return naverCafeAtricleApi.getArticles(API_URL);
    }

    public BoardDetail getBoardOne(String articleId) {
        return naverCafeAtricleApi.getNaverCafeArticleOne(articleId);
    }

    /**
     * 게시판 내용 대량 저장하기
     */
    @Transactional
    public List<Board> saveAllBoards() {
        ArrayList<Board> articles = naverCafeAtricleApi.getArticles(API_URL);
        return boardRepository.saveAll(articles);
    }

    @Transactional
    public void saveBoard(Board board) {
        boardRepository.saveBoard(board);
    }

    /**
     * 게시판 내용 가져오기 from DataBase
     */
    public List<Board> findAllBoard() {
        return boardRepository.findAllBoard();
    }

}
