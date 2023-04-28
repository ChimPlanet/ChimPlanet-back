package com.wak.chimplanet.service;

import com.wak.chimplanet.common.config.exception.NotFoundException;
import com.wak.chimplanet.dto.responseDto.BoardDetailResponseDto;
import com.wak.chimplanet.dto.responseDto.BoardResponseDto;
import com.wak.chimplanet.entity.*;
import com.wak.chimplanet.naver.NaverCafeArticleApi;
import com.wak.chimplanet.repository.BoardRepository;
import com.wak.chimplanet.repository.TagObjRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
// @RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final NaverCafeArticleApi naverCafeArticleApi;
    private final BoardRepository boardRepository;
    private final TagObjRepository tagRepository;

    private final static String API_URL = "https://apis.naver.com/cafe-web/cafe2/ArticleListV2.json?"
        + "search.clubid=27842958"
        + "&search.queryType=lastArticle"
        + "&search.menuid=148"
        + "&search.perPage=20"
        + "&search.page=";

    @Autowired
    public BoardService(NaverCafeArticleApi naverCafeArticleApi,
                        BoardRepository boardRepository, TagObjRepository tagRepository) {
        this.naverCafeArticleApi = naverCafeArticleApi;
        this.boardRepository = boardRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * 최신 게시판 받아오기
     */
    public ArrayList<Board> getAllBoardList() {
        return naverCafeArticleApi.getArticles(API_URL);
    }

    /**
     * 게시판 게시물 상세내용 가져오기
     */
    public BoardDetailResponseDto getBoardOne(String articleId) {
        BoardDetail boardDetail = naverCafeArticleApi.getNaverCafeArticleOne(articleId);
        Board board = boardRepository.findBoardWithTags(articleId).orElse(null);
        return Optional.of(BoardDetailResponseDto.from(boardDetail, board))
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
        List<TagObj> tagList = tagRepository.findAll();
        int pageSize = 20; // 저장할 페이지 갯수

        for(int i = 1; i <= 5; i++) {
            ArrayList<Board> articles = naverCafeArticleApi.getArticles(API_URL + i);
            log.info("articleSize : {} ", articles.size());
            
            // 게시글 가져오기 + 태그저장
            for(int j = 0; j < articles.size(); j++) {
                Board board = articles.get(j);
                String articleId = board.getArticleId();

                BoardDetail boardDetail = naverCafeArticleApi.getNaverCafeArticleOne(articleId);

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

                    List<TagObj> tags = categorizingTag(content, tagList);
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
     * 페이징처리 안되어있어 사용하지 않음
     */
    public List<BoardResponseDto> findAllBoard() {
        return BoardResponseDto.from(boardRepository.findAllBoard());
    }

    /**
     * 게시판 목록 가져오기 페이징 처리 추가
     */
    public Slice<BoardResponseDto> findBoardsByPaging(String lastArticleId, Pageable pageable) {
        Slice<BoardResponseDto> boards = boardRepository.findBoardsByLastArticleId(
            lastArticleId, pageable);
        log.info("Slice BoardResponse size: {} ", boards.getSize());
        return boards;
    }

    /**
     * 게시글 태그 검색
     */
    public List<BoardResponseDto> findBoardByTagIds(List<String> tagIds, String title) {

        if(tagIds.isEmpty() && title.isEmpty() && title == null) {
            throw new IllegalArgumentException("검색어를 확인해주세요");
        }

        List<BoardResponseDto> boards = boardRepository.findBoardByTagIds(tagIds, title);
        log.info("Slice BoardResponse size: {} ", boards.size());

        return boards;
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
           // log.info("검색하는 태그명: {}", tag.getTagName());
//            if (kmpSearch(content, tag.getTagName())) { // 태그 이름으로 검색
//                foundTags.add(tag.getTagName());
//            }
           if(content.contains(tag.getTagName())) {
               foundTags.add(tag.getTagName());
               findTagSet.add(tag);
           }

        }

        log.info("찾은 태그명 : {}", foundTags.toString());

       return new ArrayList<>(findTagSet);
    }

    /**
     * 모집중인 게시글 숫자 반환
     */
    public Map<String, Object> getRecruitBoardCount() {
        return null;
    }
}