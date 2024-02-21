package com.gongjakso.server.domain.banner.controller;

import com.gongjakso.server.domain.banner.dto.request.BannerReq;
import com.gongjakso.server.domain.banner.dto.response.BannerRes;
import com.gongjakso.server.domain.banner.service.BannerService;
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
@RequestMapping("/api/v1/banner")
@Tag(name = "Banner", description = "메인 페이지, 공고 페이지 등의 배너 이미지 정보 반환 API")
public class BannerController {

    private final BannerService bannerService;

    @Operation(description = "메인 페이지 공고 리스트 반환")
    @GetMapping("/main")
    public ApplicationResponse<List<BannerRes>> getMainImageList() {
        return ApplicationResponse.ok(bannerService.getMainImageList());
    }

    @Operation(description = "프로젝트 공고 페이지 배너 리스트 반환")
    @GetMapping("/project")
    public ApplicationResponse<List<BannerRes>> getProjectImageList() {
        return ApplicationResponse.ok(bannerService.getProjectImageList());
    }

    @Operation(description = "공모전 공고 페이지 배너 리스트 반환")
    @GetMapping("/content")
    public ApplicationResponse<List<BannerRes>> getContestImageList() {
        return ApplicationResponse.ok(bannerService.getContestImageList());
    }

    @Operation(description = "배너 등록 API (관리자만 가능)")
    @PostMapping("")
    public ApplicationResponse<BannerRes> registerBanner(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestPart(value = "data") BannerReq bannerReq, @RequestPart(value = "file") MultipartFile multipartFile) {
        return ApplicationResponse.ok(bannerService.registerBanner(principalDetails.getMember(), bannerReq, multipartFile));
    }

    @Operation(description = "배너 수정 API (관리자만 가능)")
    @PutMapping("/{banner_id}")
    public ApplicationResponse<BannerRes> updateBanner(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable(value = "banner_id") Long bannerId, @Valid @RequestPart(value = "data") BannerReq bannerReq, @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        return ApplicationResponse.ok(bannerService.updateBanner(principalDetails.getMember(), bannerId, bannerReq, multipartFile));
    }

    @Operation(description = "배너 게시 여부 변경 API (관리자만 가능)")
    @PatchMapping("/{banner_id}")
    public ApplicationResponse<BannerRes> changeIsPost(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable(value = "banner_id") Long bannerId) {
        return ApplicationResponse.ok(bannerService.changeIsPost(principalDetails.getMember(), bannerId));
    }

    @Operation(description = "배너 삭제 API (관리자만 가능)")
    @DeleteMapping("/{banner_id}")
    public ApplicationResponse<Void> deleteBanner(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable(value = "banner_id") Long bannerId) {
        bannerService.deleteBanner(principalDetails.getMember(), bannerId);
        return ApplicationResponse.ok();
    }
}
