package com.wak.chimplanet.repository;

import static com.wak.chimplanet.entity.QBoard.board;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
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
        int batchSize = 50;

        List<Board> savedBoards = new ArrayList<>();
        int i = 0;
        for (Board board : articles) {
            em.persist(board);
            i++;
            if (i % batchSize == 0) {
                em.flush();
                em.clear();
            }
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
    public Slice<BoardResponseDto> findBoardsByLastArticleId(String lastArticleId, Pageable pageable) {

        List<Board> boards = queryFactory.selectFrom(board)
            .leftJoin(board.boardTags, QBoardTag.boardTag).fetchJoin()
            .where(
                // no-offset 처리
                ltArticleId(lastArticleId)
            )
            .orderBy(board.articleId.desc())
            .limit(pageable.getPageSize() + 1) // imit보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true를 넣어 알림
            .fetch();

        // 무한 스크롤 처리
        return checkLastPage(pageable, boards);
    }

    public List<Board> findBoardsByReadCount() {
        return em.createQuery("select b from Board b LEFT JOIN FETCH where b.boardTags read_count >= 500", Board.class)
                .getResultList();
    }
    
    public void saveTags(List<String> tags, String articleId) {
        ;
    }

    public Optional<Board> findBoardWithTags(String articleId) {
        try {
            Board board = em.createQuery("select b from Board b join fetch b.boardTags where b.articleId = :articleId", Board.class)
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

    // no-offset 방식 처리 메서드
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
}