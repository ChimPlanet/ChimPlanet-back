package com.wak.chimplanet.naver;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 네이버 카페 게시글 API 연동
 */
@Component
public class NaverCafeAtricleApi {
    protected static final Logger logger = LoggerFactory.getLogger(NaverCafeAtricleApi.class);
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    /**
     * 네이버 API 연동
     */
    public static JsonObject getNaverCafeArticleList(String API_URL) {

        logger.info("API_URL: {}", API_URL);

        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            StringBuilder sb = new StringBuilder();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                System.out.println(sb.toString());
                JSONObject responseData = new JSONObject(sb.toString());
                return responseData;
            } else {
                System.out.println(conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject().append("error", "조회된 값이 없습니다.");
    }

    /**
     * 응답 객체를 바탕으로 전체 데이터 파싱
     */
    public static ArrayList<Board> getArticles(String API_URL) {
        JSONObject obj = getNaverCafeArticleList(API_URL).getJSONObject("message");

        ArrayList<Board> boardArrayList = new ArrayList<>();

        JSONObject result = obj.getJSONObject("result");
        JSONArray articleList = result.getJSONArray("articleList");

        for (int i = 0; i < articleList.length(); i++) {
            JSONObject data = articleList.getJSONObject(i);
            String articleId = String.valueOf(data.getLong("articleId"));
            String title = data.getString("subject");
            String viewCount = String.valueOf(data.getInt("readCount"));
            String writer = data.getString("writerNickname");
            String redirectURL = "https://cafe.naver.com/steamindiegame" + articleId;
            String thumbnailURL = data.optString("representImage");
            String regDate = dateTimeStampToString(data.getLong("writeDateTimestamp"));
            String isEnd = isEnd(title);

            logger.info("title: {}, viewCount: {}, articleId: {}, writer: {}"
                    + ", redirectURL: {}, thumbnailURL: {}, regDate: {}"
                , title, viewCount, articleId, writer, redirectURL, thumbnailURL, regDate);

            Board board = Board.builder()
                .boardTitle(title)
                .writer(writer)
                .articleId(articleId)
                .readCount(viewCount)
                .thumbnailURL(thumbnailURL)
                .redirectURL(redirectURL)
                .isEnd(isEnd)
                .regDate(regDate)
                .build();

            boardArrayList.add(board);
        }

        return boardArrayList;
    }

    public BoardDetail getNaverCafeArticleOne(String articleId) {
        String API_URL = "https://apis.naver.com/cafe-web/cafe-articleapi/v2.1/cafes/27842958/articles/"
            + articleId;
            // + "?query=&menuId=148&boardType=L&useCafeId=true&requestFrom=A";

        JsonObject obj = getNaverCafeArticleList(API_URL).getAsJsonObject("result");
        JsonObject article = obj.getAsJsonObject("article");

        logger.info(article.toString());
        logger.info("articleId: {}, contentHtml: {}", article.get("id"), article.get("contentHtml"));

        BoardDetail boardDetail = BoardDetail.builder()
                .articleId(article.get("id").getAsString())
                .content(article.get("contentHtml").getAsString())
                .redirectURL("https://cafe.naver.com/steamindiegame" + articleId)
                .build();

        return boardDetail;
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
     * timeStamp To String
     * //==유틸리티 클래스 만들어서 공통으로 처리==//
     */
    public static String dateTimeStampToString(long timeStamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault())
            .format(FORMATTER);
    }
}
