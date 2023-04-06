package com.wak.chimplanet.repository;

import static com.wak.chimplanet.entity.QBoard.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.QBoardTag;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
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
    public List<Board> findAllBoards(String lastBoardId, int size) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(lastBoardId != null) {
            booleanBuilder.and(board.articleId.lt(lastBoardId));
        }

        return queryFactory
            .selectFrom(board)
            .leftJoin(board.boardTags, QBoardTag.boardTag)
            .where(booleanBuilder)
            .orderBy(board.articleId.desc())
            .limit(20)
            .fetch();
    }

    public List<Board> findBoardsByReadCount() {
        return em.createQuery("select b from Board b LEFT JOIN FETCH where read_count >= 500", Board.class)
                .getResultList();
    }
    
    public void saveTags(List<String> tags, String articleId) {
        ;
    }

    public Board findBoardWithTags(String articleId) {
        return em.createQuery("select b from Board b join fetch b.boardTags where b.articleId = :articleId", Board.class)
                .setParameter("articleId", articleId)
                .getSingleResult();
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
}