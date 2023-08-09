package com.wak.chimplanet.naver;

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
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;

/**
 * 네이버 카페 게시글 API 연동
 */
@Component
public class NaverCafeArticleApi {
    protected static final Logger logger = LoggerFactory.getLogger(NaverCafeArticleApi.class);
    
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
                conn.disconnect();
                JsonObject responseData = JsonParser.parseString(sb.toString()).getAsJsonObject();
                logger.info("ResponseData: {}", responseData.toString());
                return responseData;
            } else {
                logger.error("ResponseMessage: {}", conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("error", "조회된 게시물이 없습니다.");

        return obj;
    }

    /**
     * 응답 객체를 바탕으로 전체 데이터 파싱
     */
    public static ArrayList<Board> getArticles(String API_URL) {
        JsonObject obj  = getNaverCafeArticleList(API_URL).getAsJsonObject("message");
        ArrayList<Board> boardArrayList = new ArrayList<>();

        JsonObject result = obj.getAsJsonObject("result");
        JsonArray articleList = result.getAsJsonArray("articleList");

        for (int i = 0; i < articleList.size(); i++) {
            JsonObject data = articleList.get(i).getAsJsonObject();
            String articleId = String.valueOf(data.get("articleId").getAsLong());
            String title = data.get("subject").getAsString()
                    .replaceAll("&lt;", "<")
                    .replaceAll("&gt;", ">");
            Integer readCount = data.get("readCount").getAsInt();
            String writer = data.get("writerNickname").getAsString();
            String redirectURL = "https://cafe.naver.com/steamindiegame/" + articleId;
            String thumbnailURL = null;
            if(data.has("representImage")) {
                thumbnailURL = data.get("representImage").getAsString();
            }
            String regDate = dateTimeStampToString(data.get("writeDateTimestamp").getAsLong());
            String isEnd = isEnd(title);
            String teamOperationInfo;
            if(!data.has("headId")) teamOperationInfo = isJobSearching(null);
            else teamOperationInfo = isJobSearching(data.get("headId").getAsString());


            logger.info("title: {}, viewCount: {}, articleId: {}, writer: {}"
                    + ", redirectURL: {}, thumbnailURL: {}, regDate: {}, teamOperationInfo: {}"
                , title, readCount, articleId, writer, redirectURL, thumbnailURL, regDate, teamOperationInfo);

            Board board = Board.builder()
                    .boardTitle(title)
                    .writer(writer)
                    .articleId(articleId)
                    .readCount(readCount)
                    .thumbnailURL(thumbnailURL)
                    .redirectURL(redirectURL)
                    .isEnd(isEnd)
                    .regDate(LocalDateTime.parse(regDate, FORMATTER))
                    .teamOperationInfo(teamOperationInfo)
                    .unauthorized("N")
                    .build();

            boardArrayList.add(board);
        }

        return boardArrayList;
    }


    /**
     * 공고 게시물 가져오기
     * @param articleId
     * @return
     */
    public BoardDetail getNaverCafeArticleOne(String articleId) {
        String API_URL = "https://apis.naver.com/cafe-web/cafe-articleapi/v2.1/cafes/27842958/articles/"
            + articleId
            + "?query=&menuId=148&boardType=L&useCafeId=true&requestFrom=A";

        JsonObject obj = getNaverCafeArticleList(API_URL);

        if(obj.has("error")) {
            logger.warn("{} 권한이 없는 게시물 입니다.", articleId);
            return null;
        }

        JsonObject article = obj.getAsJsonObject("result").getAsJsonObject("article");
        JsonObject writer = article.getAsJsonObject("writer");

        logger.info(article.toString());
        logger.info("articleId: {}, contentHtml: {}", article.get("id"), article.get("contentHtml"));

        return BoardDetail.builder()
            .articleId(article.get("id").getAsString())
            .content(article.get("contentHtml").getAsString())
            .boardTitle(article.get("subject").getAsString())
            .writer(writer.get("nick").getAsString())
            .profileImageUrl(writer.get("image").getAsJsonObject().get("url").getAsString())
            .redirectURL("https://cafe.naver.com/steamindiegame" + articleId)
            .readCount(article.get("readCount").getAsInt())
            .build();
    }

    /**
     * 게시물의 공고가 마감인지 확인하는 메소드
     */
    private static String isEnd(String title) {
        String END_TITLE = "마감";
        String END_TITLE_SECOND = "완료";
        if(title.contains(END_TITLE) || title.contains(END_TITLE_SECOND)) return "END";
        return "ING";
    }

    /**
     * 게시물의 내용이 구인인지 구직인지 판별
     * 311 : 팀 창설, 312 : 팀 구합니다
     * @return recruit : 팀 창설, searching : 팀 구직, noHeadName : ㅁㅁㄹ
     */
    private static String isJobSearching(String headName) {
        if(headName == null ) return "noHeadName";

        if(headName.equals("311")) {
            return "recruit";
        }
        if(headName.equals("312")) {
            return "searching";
        }

        return "noHeadName";
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
