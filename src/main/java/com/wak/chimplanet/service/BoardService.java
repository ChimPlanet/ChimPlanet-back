package com.wak.chimplanet.service;

import com.wak.chimplanet.common.config.exception.NotFoundException;
import com.wak.chimplanet.dto.responseDto.BoardDetailResponseDTO;
import com.wak.chimplanet.entity.*;
import com.wak.chimplanet.naver.NaverCafeAtricleApi;
import com.wak.chimplanet.repository.BoardRepository;
import com.wak.chimplanet.repository.TagObjRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
// @RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final NaverCafeAtricleApi naverCafeAtricleApi;
    private final BoardRepository boardRepository;
    private final TagObjRepository tagRepository;

    private final static String API_URL = "https://apis.naver.com/cafe-web/cafe2/ArticleListV2.json?"
        + "search.clubid=27842958"
        + "&search.queryType=lastArticle"
        + "&search.menuid=148"
        + "&search.perPage=20"
        + "&search.page=";

    @Autowired
    public BoardService(NaverCafeAtricleApi naverCafeAtricleApi,
                        BoardRepository boardRepository, TagObjRepository tagRepository) {
        this.naverCafeAtricleApi = naverCafeAtricleApi;
        this.boardRepository = boardRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * 최신 게시판 받아오기
     */
    public ArrayList<Board> getAllBoardList() {
        return naverCafeAtricleApi.getArticles(API_URL);
    }

    /**
     * 게시판 게시물 상세내용 가져오기
     */
    public BoardDetailResponseDTO getBoardOne(String articleId) {
        BoardDetail boardDetail = naverCafeAtricleApi.getNaverCafeArticleOne(articleId);
        Board board = boardRepository.findBoardWithTags(articleId).orElse(null);
        return Optional.of(BoardDetailResponseDTO.from(boardDetail, board))
            .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));
    }

    /**
     * 게시판 내용 대량 저장하기
     *  10페이지씩 순회하면서 데이터 저장
     *  Batch 쪽으로 추후 변경 예정
     */
    @Transactional
    public List<Board> saveAllBoards() {
        ArrayList<Board> boards = new ArrayList<>();
        int pageSize = 20; // 저장할 페이지 갯수

        for(int i = 1; i <= 5; i++) {
            ArrayList<Board> articles = naverCafeAtricleApi.getArticles(API_URL + i);
            log.info("articleSize : {} ", articles.size());

/*
            // 리팩토리중인 소스코드
            for(Board board : articles) {
                String articleId = board.getArticleId();
                BoardDetail boardDetail = naverCafeAtricleApi.getNaverCafeArticleOne(articleId);

                if(boardDetail == null) {
                    board.setUnauthorized("Y");
                } else {
                    List<TagObj> tags = categorizingTag(boardDetail.getContent());
                    List<BoardTag> boardTags = new ArrayList<>();

                    for(TagObj tag : tags) {
                        BoardTag boardTag = BoardTag.createBoardTag(tag, board);
                        board.addBoardTag(boardTag);
                        boardTags.add(boardTag);
                    }
                }

            }*/

            // 게시글 가져오기 + 태그저장 => 일단 돌아만 가게 만든 소스코드
            for(int j = 0; j < articles.size(); j++) {
                Board board = articles.get(j);
                String articleId = board.getArticleId();

                BoardDetail boardDetail = naverCafeAtricleApi.getNaverCafeArticleOne(articleId);

                String unauthorized = "N"; // 접근권한 여부
                if(boardDetail == null) {
                    unauthorized = "Y";
                    board.setUnauthorized(unauthorized);

                    // System.out.println("권한 확인 : " + board.getUnauthorized());
                    List<BoardTag> boardTags = new ArrayList<>();
                    Board newBoard = Board.createBoardWithTag(board, boardTags, unauthorized);
                    Board existingBoard = boardRepository.findById(articleId).orElse(null); // 기존에 같은 ID를 가지고 있는 경우 UPDATE 쿼리를 날림

                    if(existingBoard != null) {
                        log.info("기존에 있던 게시물 : {}", board.getArticleId());
                        board.updateBoard(board, boardTags);
                    } else {
                        boards.add(newBoard);
                    }
                } else {
                    String content = Optional.ofNullable(boardDetail.getContent()).orElse(null);

                    List<TagObj> tags = categorizingTag(content);
                    List<BoardTag> boardTags = new ArrayList<>();

                    for(TagObj tag : tags) {
                        BoardTag boardTag = BoardTag.createBoardTag(tag, board);
                        board.addBoardTag(boardTag); // Board의 연관관계 메서드로 BoardTag 추가
                        boardTags.add(boardTag); // BoardTag 리스트에도 추가
                    }

                    Board newBoard = Board.createBoardWithTag(board, boardTags, unauthorized);
                    Board existingBoard = boardRepository.findById(articleId).orElse(null); // 기존에 같은 ID를 가지고 있는 경우 UPDATE 쿼리를 날림

                    if(existingBoard != null) {
                        log.info("기존에 있던 게시물 : {}", board.getArticleId());
                        board.updateBoard(board, boardTags);
                    } else {
                        boards.add(newBoard);
                    }
                }
            }

            boardRepository.saveAll(boards);
        }
        return boards;
    }

    @Transactional
    public void saveBoard(Board board) {
        boardRepository.saveBoard(board);
    }

    /**
     * 게시판 목록 가져오기 from DataBase
     */
    public List<Board> findAllBoard() {
        List<Board> allBoard = boardRepository.findAllBoard();
        return allBoard;
    }

     /**
     * 게시판 목록 인기글 가져오기
     */
    public List<Board> findBoardsByReadCount() {
        return boardRepository.findBoardsByReadCount();
    }

    /**
     * 게시글에서 태그 리스트 분류하기
     */
    public List<TagObj> categorizingTag(String content) {
        if(content.isEmpty()) return null;

        // 문장에서 찾은 태그명
        Set<String> foundTags = new HashSet<>();
        List<TagObj> tags = tagRepository.findALl();

       for(TagObj tag : tags) {
           // log.info("검색하는 태그명: {}", tag.getTagName());
//            if (kmpSearch(content, tag.getTagName())) { // 태그 이름으로 검색
//                foundTags.add(tag.getTagName());
//            }
           if(content.contains(tag.getTagName())) {
               foundTags.add(tag.getTagName());
           }

        }

        log.info("찾은 태그명 : {}", foundTags.toString());

       return tagRepository.findAllByName(new ArrayList<>(foundTags));
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
