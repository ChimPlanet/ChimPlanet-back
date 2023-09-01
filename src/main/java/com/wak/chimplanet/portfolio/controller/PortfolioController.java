package com.wak.chimplanet.portfolio.controller;

import com.wak.chimplanet.portfolio.domain.Portfolio;
import com.wak.chimplanet.portfolio.service.PortfolioService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 각진설탕
 * @since 2023.08.31
 * <p>
 * 포트폴리오 기능부터 domain driven - 적용예정
 */
@RestController
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;


    @GetMapping("/api/portfolio")
    public ResponseEntity<?> findPortfolios() {
        portfolioService.findPortfolios();
    }

    @GetMapping("/api/portfolio/{portfolioId}")
    public ResponseEntity<?> findPortfolioDetail(
        @PathVariable("portfolioId") @Valid Long portfolioId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(portfolioService.findPortfolioDetailById(portfolioId));
    }
}
