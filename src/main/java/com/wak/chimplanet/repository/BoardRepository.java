package com.wak.chimplanet.repository;

import com.wak.chimplanet.entity.Board;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
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
        return em.createQuery("select b from Board b", Board.class)
            .getResultList();
    }

    public List<Board> findBoardsByReadCount() {
        return em.createQuery("select b from Board b where read_count >= 500", Board.class)
            .getResultList();
    }

    public void saveTags(List<String> tags, String articleId) {
        ;
    }
}
