package com.wak.chimplanet.naver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wak.chimplanet.entity.Board;
import java.util.Map;
import java.util.stream.Collectors;
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
    private static final String clubId = "27842958"; // 왁물원
    private static final String menuId = "148"; // 인력관리게시판 menuId
    private static final String GOD = "우왁굳"; // 형

    /**
     * 게시판 게시글 목록 크롤링
     */
    public List<Board> getNaverCafeBoard() {
        List<Board> boardList = new ArrayList<>();
        String iframeUrl = "https://cafe.naver.com/ArticleList.nhn?" +
                "search.clubid="+ clubId +
                "&search.menuid=" + menuId +
                "&search.boardtype=C";
                // "search.page=2" 페이징 처리

        logger.info("iframeUrl  = {} ", iframeUrl);

        try {
            // iframe 페이지 HTML 코드 가져오기
            Document iframeDocument = Jsoup.connect(iframeUrl).get();
            // 게시물 목록 요소 가져오기
            Elements articleElements = iframeDocument.select("#main-area > ul.article-movie-sub > li");

            for (Element articleElement : articleElements) {
                String redirectURL = articleElement.select("a").first().attr("href");
                String boardId = parseQueryString(redirectURL).get("articleid");
                String title = articleElement.selectFirst(".inner").text();
                String articleId = createArticleURL(boardId);
                String regDate = articleElement.select(".date").text();
                String writer = articleElement.select(".m-tcol-c").text();
                String viewCount = articleElement.select(".num").text().split(" ")[1];
                String thumbnail = null;
                String isEnd = isEnd(title);
                if(!articleElement.select("div.movie-img > a > img").isEmpty()) {
                    thumbnail = articleElement.select("div.movie-img > a > img").first().attr("src");
                }

                logger.debug("boardId : {}, Title: {}, articleId = {}, likeCount = {}, viewCount = {}, " +
                        "regDate = {}, writer = {}, thumbnail = {}, isEnd = {}",
                    boardId, title, articleId, "likeCount", viewCount, regDate, writer, thumbnail, isEnd);

                /*Board board = Board.builder()
                    .
                        .title(title)
                        .articleId(articleId)
                        .regDate(regDate)
                        .viewCount(viewCount)
                        .likeCount(thumbnail)
                        .writer(writer)
                        .imgUrl(null)
                        .endStr(isEnd)
                        .build();

                boardList.add(board);*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boardList;
    }

    /**
     * 공고가 마감인지 확인하는 메소드
     */
    private static String isEnd(String title) {
        String END_TITLE = "마감";
        if(title.contains(END_TITLE)) return "END";
        return "ING";
    }

    /**
     * url 파싱
     * 추후 Util Class 생성 후 이동
     */
    private static Map<String, String> parseQueryString(String queryString) {
        return Arrays.stream(queryString.split("&"))
            .map(params -> params.split("=", 2))
            .collect(Collectors.toMap(param -> param[0], param -> param.length > 1 ? param[1] : null));
    }

    /**
     * 게시글 이동 URL
     */
    private static String createArticleURL(String articleId) {
        return "https://cafe.naver.com/steamindiegame/" + articleId;
    }
}