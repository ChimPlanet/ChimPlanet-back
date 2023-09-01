package com.wak.chimplanet.portfolio.repository;

import com.wak.chimplanet.portfolio.domain.Portfolio;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PortfolioRepository {

    private final EntityManager em;

    public List<Portfolio> findPortfolios() {
        return em.createQuery(
            "select p from Portfolio p", Portfolio.class
        ).getResultList();
    }

    public Portfolio findPortfolioDetailById(Long portfolioId) {
        return em.createQuery(
                "select p from Portfolio p where portfolioId = :portfolioId", Portfolio.class)
            .setParameter("portfolioId", portfolioId)
            .getSingleResult();
    }
}
