package com.gongjakso.server.domain.portfolio.controller;

import com.gongjakso.server.domain.portfolio.dto.request.PortfolioReq;
import com.gongjakso.server.domain.portfolio.dto.response.PortfolioRes;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.portfolio.service.PortfolioService;
import com.gongjakso.server.global.common.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/mypage/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Operation(description = "포트폴리오 등록 API")
    @PostMapping("")
    public ApplicationResponse<Portfolio> registerPortfolio(@Valid @RequestBody PortfolioReq portfolioReq) {
        return ApplicationResponse.ok(portfolioService.registerPortfolio(portfolioReq));
    }

    @Operation(description = "포트폴리오 상세 조회 API")
    @GetMapping("/{portfolio_id}")
    public ApplicationResponse<Portfolio> getPortfolio(@PathVariable("portfolio_id") Long portfolioId) {
        return ApplicationResponse.ok(portfolioService.getPortfolio(portfolioId));
    }

    @Operation(description = "포트폴리오 수정 API")
    @PutMapping("/{portfolio_id}")
    public ApplicationResponse<PortfolioRes> updatePortfolio(@PathVariable("portfolio_id") Long portfolioId, @Valid @RequestBody PortfolioReq portfolioReq) {
        return ApplicationResponse.ok(portfolioService.updatePortfolio(portfolioId, portfolioReq));
    }

    @Operation(description = "포트폴리오 삭제 API")
    @DeleteMapping("/{portfolio_id}")
    public ApplicationResponse<Void> deletePortfolio(@PathVariable("portfolio_id") Long portfolioId) {
        portfolioService.deletePortfolio(portfolioId);

        return ApplicationResponse.ok();
    }
}
