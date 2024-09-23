package com.gongjakso.server.domain.portfolio.controller;

import com.gongjakso.server.domain.portfolio.dto.request.PortfolioReq;
import com.gongjakso.server.domain.portfolio.dto.response.ExistPortfolioRes;
import com.gongjakso.server.domain.portfolio.dto.response.PortfolioRes;
import com.gongjakso.server.domain.portfolio.dto.response.SimplePortfolioRes;
import com.gongjakso.server.domain.portfolio.enumerate.DataType;
import com.gongjakso.server.domain.portfolio.service.PortfolioService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Operation(description = "포트폴리오 파일 및 링크 업로드 API")
    @PostMapping("/exist-portfolio")
    public ApplicationResponse<Void> updateExistPortfolio(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestPart(required = false,name = "file") MultipartFile file, @RequestPart(required = false,name = "notionUri") String notionUri){
        portfolioService.saveExistPortfolio(principalDetails.getMember(),file,notionUri);
        return ApplicationResponse.ok();
    }

    @Operation(description = "포트폴리오 파일 및 링크 업로드 삭제 API")
    @DeleteMapping("/exist-portfolio/{portfolio_id}")
    public ApplicationResponse<Void> deleteExistPortfolio(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("portfolio_id") Long portfolioId, @RequestParam(name = "dataType") DataType dataType){
        portfolioService.deleteExistPortfolio(principalDetails.getMember(),portfolioId,dataType);
        return ApplicationResponse.ok();
    }

    @Operation(description = "포트폴리오 파일 및 링크 업로드 업데이트 API")
    @PatchMapping("/exist-portfolio/{portfolio_id}")
    public ApplicationResponse<Void> updateExistPortfolio(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("portfolio_id") Long portfolioId, @RequestPart(required = false,name = "file") MultipartFile file, @RequestPart(required = false,name = "notionUri") String notionUri){
        portfolioService.updateExistPortfolio(principalDetails.getMember(),portfolioId,file,notionUri);
        return ApplicationResponse.ok();
    }

    @Operation(description = "포트폴리오 파일 및 링크 업로드 가져오기 API")
    @GetMapping("/exist-portfolio/{portfolio_id}")
    public ApplicationResponse<ExistPortfolioRes> findExistPortfolio(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("portfolio_id") Long portfolioId, @RequestParam(name = "dataType") DataType dataType){
        return ApplicationResponse.ok(portfolioService.findExistPorfolio(principalDetails.getMember(),portfolioId,dataType));
    }


}
