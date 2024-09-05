package com.gongjakso.server.domain.portfolio.controller;

import com.gongjakso.server.domain.portfolio.dto.request.PortfolioReq;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.portfolio.service.PortfolioService;
import com.gongjakso.server.global.common.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
}
