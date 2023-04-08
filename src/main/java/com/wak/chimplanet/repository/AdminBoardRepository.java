package com.wak.chimplanet.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wak.chimplanet.dto.responseDto.admin.AdminGetBoardResponseDto;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.QBoard;
import com.wak.chimplanet.entity.QBoardTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminBoardRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public Optional<AdminGetBoardResponseDto> findBoardWithTags(String articleId) {
        Board board = queryFactory
                .selectFrom(QBoard.board)
                .leftJoin(QBoard.board.boardTags, QBoardTag.boardTag).fetchJoin()
                .where(QBoard.board.articleId.eq(articleId))
                .fetchOne();

        return Optional.ofNullable(board)
                .map(AdminGetBoardResponseDto::new);
    }
}
