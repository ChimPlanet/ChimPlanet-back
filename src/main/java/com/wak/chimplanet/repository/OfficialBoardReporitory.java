package com.wak.chimplanet.repository;

import com.wak.chimplanet.entity.OfficialBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OfficialBoardReporitory {
    private final EntityManager em;

    public List<OfficialBoard> findAll() {
        return em.createQuery("select o from OfficialBoard o", OfficialBoard.class).getResultList();
    }

    public void save(OfficialBoard officialBoard) {
        em.persist(officialBoard);
    }

    public void deleteById(String articleId) {

        OfficialBoard officialBoard = em.find(OfficialBoard.class, articleId);


        System.out.println(" ============= officialBoard" + officialBoard.getArticleId());
        System.out.println(officialBoard);
        System.out.println(" ============= officialBoard =============");
        em.remove(officialBoard);
    }
}
