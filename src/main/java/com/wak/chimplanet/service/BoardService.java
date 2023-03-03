package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.naver.NaverCafeAtricleApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    // private final NaverCafeCrawler naverCafeCrawler;
    private final NaverCafeAtricleApi naverCafeAtricleApi;

    private final static String API_URL = "https://apis.naver.com/cafe-web/cafe2/ArticleListV2.json?"
        + "search.clubid=27842958"
        + "&search.queryType=lastArticle"
        + "&search.menuid=148"
        + "&search.page=1&search.perPage=20"; // 페이징 + 갯수 처리 필요

    public ArrayList<Board> getAllBoardList() {
        return naverCafeAtricleApi.getArticles(API_URL);
    }

    public BoardDetail getBoardOne(String articleId) {
        return naverCafeAtricleApi.getNaverCafeArticleOne(articleId);
    }
}
