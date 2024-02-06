package com.gongjakso.server.domain.banner.controller;

import com.gongjakso.server.domain.banner.dto.response.BannerRes;
import com.gongjakso.server.domain.banner.service.BannerService;
import com.gongjakso.server.global.common.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
