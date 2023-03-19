package com.wak.chimplanet.service;

import com.wak.chimplanet.common.config.exception.NotFoundException;
import com.wak.chimplanet.dto.responseDto.BoardDetailResponseDTO;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.entity.BoardTag;
import com.wak.chimplanet.entity.Tag;
import com.wak.chimplanet.naver.NaverCafeAtricleApi;
import com.wak.chimplanet.repository.BoardRepository;

import java.util.*;
import java.util.stream.Collectors;

import com.wak.chimplanet.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// @RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final NaverCafeAtricleApi naverCafeAtricleApi;
    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;

    private final static String API_URL = "https://apis.naver.com/cafe-web/cafe2/ArticleListV2.json?"
        + "search.clubid=27842958"
        + "&search.queryType=lastArticle"
        + "&search.menuid=148"
        + "&search.perPage=20" // 페이징 + 갯수 처리 필요
        + "&search.page=";

    @Autowired
    public BoardService(NaverCafeAtricleApi naverCafeAtricleApi,
                        BoardRepository boardRepository, TagRepository tagRepository) {
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
        Board board = boardRepository.findBoardWithTags(articleId);
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

        for(int i = 1; i <= 1; i++) {
            ArrayList<Board> articles = naverCafeAtricleApi.getArticles(API_URL + i);
            log.info("articleSize : {} ", articles.size());

            // 게시글 가져오기 + 태그저장
            for(int j = 0; j < articles.size(); j++) {
                Board board = articles.get(j);
                String articleId = board.getArticleId();
                BoardDetail boardDetail = naverCafeAtricleApi.getNaverCafeArticleOne(articleId);

                /*
                    예외 처리되는 경우 추가 UNAUTHORIZED 컬럼을 추가해줘야함
                    + H2DB 경우 Default 값이 엔티티에서 제대로 설정이 안됨...
                */
                if(boardDetail == null) continue; // 개선 필요

                String content = boardDetail.getContent();

                List<Tag> tags = categorizingTag(content);
                List<BoardTag> boardTags = new ArrayList<>();

                for(Tag tag : tags) {
                    BoardTag boardTag = BoardTag.createBoardTag(tag, board);
                    board.addBoardTag(boardTag); // Board의 연관관계 메서드로 BoardTag 추가
                    boardTags.add(boardTag);
                }

                Board newBoard = Board.createBoardWithTag(board, boardTags);
                System.out.println("newBoard = " + newBoard.toString());
                boards.add(newBoard);
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
        return boardRepository.findAllBoard();
    }

    public List<Board> findBoardsByReadCount() {
        return boardRepository.findBoardsByReadCount();
    }


    /**
     * 게시글에서 태그 리스트 분류하기
     */
    public List<Tag> categorizingTag(String content) {
        // 문장에서 찾은 태그명
        Set<String> foundTags = new HashSet<>();
        List<Tag> tags = tagRepository.findALl();

       for(Tag tag : tags) {
           log.info("검색하는 태그명: {}",tag.getTagName());
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
