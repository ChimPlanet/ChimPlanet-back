package com.wak.chimplanet.repository;

import static com.wak.chimplanet.entity.QBoard.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.QBoard;
import com.wak.chimplanet.entity.QBoardTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class AdminBoardRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AdminBoardRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Optional<Board> findBoardWithTags(String articleId) {
        Board board = queryFactory
                .selectFrom(QBoard.board)
                .leftJoin(QBoard.board.boardTags, QBoardTag.boardTag).fetchJoin()
                .where(QBoard.board.articleId.eq(articleId))
                .fetchOne();

        return Optional.ofNullable(board);
    }

    public void deleteBoardTags(String articleId) {
        em.createQuery("delete from BoardTag bt where bt.board.articleId = :articleId")
                .setParameter("articleId", articleId)
                .executeUpdate();
    }
}
