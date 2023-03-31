package com.wak.chimplanet.repository;

import com.wak.chimplanet.entity.Board;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

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
        return em.createQuery( "SELECT b FROM Board b LEFT JOIN FETCH b.boardTags", Board.class)
                .getResultList();
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