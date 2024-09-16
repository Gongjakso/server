package com.gongjakso.server.domain.portfolio.controller;

import com.gongjakso.server.domain.portfolio.dto.request.PortfolioReq;
import com.gongjakso.server.domain.portfolio.dto.response.PortfolioRes;
import com.gongjakso.server.domain.portfolio.dto.response.SimplePortfolioRes;
import com.gongjakso.server.domain.portfolio.service.PortfolioService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/mypage/portfolio")
@Tag(name = "Portfolio", description = "포트폴리오 관련 API")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Operation(description = "포트폴리오 등록 API")
    @PostMapping("")
    public ApplicationResponse<PortfolioRes> registerPortfolio(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                               @Valid @RequestBody PortfolioReq portfolioReq) {
        return ApplicationResponse.ok(portfolioService.registerPortfolio(principalDetails.getMember(), portfolioReq));
    }

    @Operation(description = "포트폴리오 상세 조회 API")
    @GetMapping("/{portfolio_id}")
    public ApplicationResponse<PortfolioRes> getPortfolio(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                          @PathVariable("portfolio_id") Long portfolioId) {
        return ApplicationResponse.ok(portfolioService.getPortfolio(principalDetails.getMember(), portfolioId));
    }

    @Operation(description = "포트폴리오 수정 API")
    @PutMapping("/{portfolio_id}")
    public ApplicationResponse<PortfolioRes> updatePortfolio(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                             @PathVariable("portfolio_id") Long portfolioId,
                                                             @Valid @RequestBody PortfolioReq portfolioReq) {
        return ApplicationResponse.ok(portfolioService.updatePortfolio(principalDetails.getMember(), portfolioId, portfolioReq));
    }

    @Operation(description = "포트폴리오 삭제 API")
    @DeleteMapping("/{portfolio_id}")
    public ApplicationResponse<Void> deletePortfolio(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @PathVariable("portfolio_id") Long portfolioId) {
        portfolioService.deletePortfolio(principalDetails.getMember(), portfolioId);

        return ApplicationResponse.ok();
    }

    @Operation(description = "내 포트폴리오 리스트 조회 API")
    @GetMapping("/my")
    public ApplicationResponse<List<SimplePortfolioRes>> getMyPortfolios(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApplicationResponse.ok(portfolioService.getMyPortfolios(principalDetails.getMember()));
    }
}
