package com.wak.chimplanet.batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wak.chimplanet.entity.Board;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NaverCafeCrawler {
    protected static final Logger logger = LoggerFactory.getLogger(NaverCafeCrawler.class);

    // 카페 정보
    private static final String cafeName = "steamindiegame";
    private static final String clubId = "27842958"; // 스팀인디게임 카페 ID
    private static final String menuId = "148"; // 인디게임게시판 ID
    // private static final String rowCount = "50"; // 한번에 가져올 게시물 수

    public List<Board> getNaverCafeBoard() {
        List<Board> boardList = new ArrayList<>();
        String iframeUrl = "https://cafe.naver.com/ArticleList.nhn?" +
                "search.clubid="+ clubId +
                "&search.menuid=" + menuId +
                "&userDisplay=50" +
                "&search.boardtype=L";

        logger.info("iframeUrl  = {} ", iframeUrl);

        try {
            // iframe 페이지 HTML 코드 가져오기
            Document iframeDocument = Jsoup.connect(iframeUrl).get();
            // 게시물 목록 요소 가져오기
            Elements articleElements = iframeDocument.select("#main-area > div.article-board.m-tcol-c > table > tbody > tr");

            for (Element articleElement : articleElements) {
                String writer = articleElement.select(".m-tcol-c").text();
                if(writer.equals("우왁굳")) continue; // 공지게시물 스킵
                String boardId = articleElement.select(".inner_number").text();
                String title = articleElement.selectFirst("a.article").text();
                String articleId = articleElement.select("a").first().attr("href");
                String regDate = articleElement.select(".td_date").text();
                String viewCount = articleElement.select(".td_view").text();
                String likeCount = articleElement.select(".td_likes").text();
                String endStr = isEnd(title);

                logger.info("boardId : {}, Title: {}, articleId = {}, likeCount = {}, " +
                                "viewCount = {}, regDate = {}, writer = {}, endStr = {}",
                        boardId, title, articleId, likeCount, viewCount, regDate, writer, endStr);

                Board board = Board.builder()
                        .boardId(boardId)
                        .title(title)
                        .articleId(articleId)
                        .regDate(regDate)
                        .viewCount(viewCount)
                        .likeCount(likeCount)
                        .writer(writer)
                        .imgUrl(null)
                        .endStr(endStr)
                        .build();

                boardList.add(board);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return boardList;
    }

    private static String isEnd(String title) {
        String END_TITLE = "마감";
        if(title.contains(END_TITLE)) return "END";
        return "ING";
    }
}