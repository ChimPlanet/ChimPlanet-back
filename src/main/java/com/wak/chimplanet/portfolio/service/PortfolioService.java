package com.wak.chimplanet.portfolio.service;

import com.wak.chimplanet.portfolio.domain.Portfolio;
import com.wak.chimplanet.portfolio.repository.PortfolioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public List<Portfolio> findPortfolios() {
        return portfolioRepository.findPortfolios();
    }

    public Portfolio findPortfolioDetailById(Long portfolioId) {
        return portfolioRepository.findPortfolioDetailById(portfolioId);
    }


}
