package com.wak.chimplanet.repository;

import static com.wak.chimplanet.entity.QBoard.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wak.chimplanet.dto.responseDto.BoardResponseDto;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.QBoardTag;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public BoardRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Board> saveAll(List<Board> articles) {
        List<Board> savedBoards = new ArrayList<>();
        for (Board board : articles) {
            em.persist(board);
            savedBoards.add(board);
        }
        return savedBoards;
        /*return IntStream.range(0, (articles.size() + batchSize - 1) / batchSize)
            .mapToObj(i -> articles.subList(i * batchSize, Math.min((i + 1) * batchSize, articles.size())))
            .flatMap(Collection::stream)
            .peek(board -> em.persist(board))
            .collect(Collectors.toList());*/
    }

    public void saveBoard(Board board) {
        em.persist(board);
    }

    public List<Board> findAllBoard() {
        // JPQL 사용쿼리
        return em.createQuery( "SELECT b FROM Board b LEFT JOIN FETCH b.boardTags", Board.class)
                .getResultList();
    }

    /**
     * 무한스크롤 구현
     */
    public Slice<BoardResponseDto> findBoardsByLastArticleId(
            String sortColumn, String lastArticleId, String lastValue, Pageable pageable, String isEnd) {
        BooleanExpression isEndCondition = null;
        BooleanExpression ltReadCountCondition = null;
        BooleanExpression ltArticleIdCondition = null;

        if(sortColumn.equals("readCount") && lastValue != null) {
            // 조회수는 unique 값이 아니므로 작거나 같음 조건
            ltReadCountCondition = board.readCount.loe(Long.parseLong(lastValue));
            ltArticleIdCondition = board.articleId.ne(lastArticleId);
        } else if(sortColumn.equals("articleId")) {
            ltArticleIdCondition = ltArticleId(lastArticleId);
        }

        if(isEnd != null) isEndCondition = board.isEnd.eq(isEnd);

        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.boardTags, QBoardTag.boardTag).fetchJoin()
                .where(
                        isEndCondition,
                        ltReadCountCondition,
                        ltArticleIdCondition
                )
                .orderBy(toOrderSpecifiers(pageable))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, boards);
    }

    /**
     * TAG ID 기준으로 검색
     * tagIds 가 있는 경우에는 tagIds 로 검색
     * title 이 있는 경우에는 title 로 검색
     */
    public List<BoardResponseDto> findBoardByTagIds(List<Long> tagIds, String title) {
        JPQLQuery<Board> query = queryFactory.selectFrom(board)
                .leftJoin(board.boardTags, QBoardTag.boardTag).fetchJoin()
                .distinct();

        BooleanBuilder whereBuilder = new BooleanBuilder();

        if (tagIds != null && !tagIds.isEmpty()) {
            whereBuilder.or(board.boardTags.any().tagObj.childTagId.in(tagIds));
        }

        if (title != null && !title.trim().isEmpty()) {
            whereBuilder.or(board.boardTitle.containsIgnoreCase(title));
        }

        List<Board> boards = query
                .where(whereBuilder)
                .orderBy(board.articleId.desc())
                .fetchResults()
                .getResults();

        return BoardResponseDto.from(boards);
    }

    public List<Board> findBoardsByReadCount() {
        return em.createQuery("select b from Board b LEFT JOIN FETCH  b.boardTags where read_count >= 500", Board.class)
                .getResultList();
    }

    public Optional<Board> findBoardWithTags(String articleId) {
        try {
            Board board = em.createQuery("select b from Board b left join fetch b.boardTags where b.articleId = :articleId", Board.class)
                .setParameter("articleId", articleId)
                .getSingleResult();
            return Optional.of(board);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Board> findById(String articleId) {
        Optional<Board> board = null;
        try {
            board = Optional.ofNullable(em.createQuery("select b from Board b where b.articleId = :articleId", Board.class)
                .setParameter("articleId", articleId)
                .getSingleResult());
        } catch (NoResultException e) {
            board = Optional.empty();
        } finally {
        return board;
    }
}

    /**
     * no-offset 방식 처리 메서드
     */
    private BooleanExpression ltArticleId(String lastArticleId) {
        return StringUtils.isNullOrEmpty(lastArticleId) ? null : board.articleId.lt(lastArticleId);
    }

    /**
     * 마지막 페이지인지 확인 메서드
     */
    private Slice<BoardResponseDto> checkLastPage(Pageable pageable, List<Board> results) {
        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(BoardResponseDto.from(results), pageable, hasNext);
    }

    /**
     * OrderSpecifier 를 쿼리로 변환하여 정렬조건 추가
     * @param pageable
     * @return
     */
    private OrderSpecifier<?> toOrderSpecifiers(Pageable pageable) {
        // 정렬조건 Null 체크
        if(!pageable.getSort().isEmpty()) {
            for(Sort.Order order : pageable.getSort()) {
                switch (order.getProperty()) {
                    case "articleId" :
                        return new OrderSpecifier(Order.DESC, board.articleId);
                    case "readCount" :
                        return new OrderSpecifier(Order.DESC, board.readCount);
                    case "regDate" :
                        return new OrderSpecifier(Order.DESC, board.regDate);
                }
            }
        }

        return null;
    }
}