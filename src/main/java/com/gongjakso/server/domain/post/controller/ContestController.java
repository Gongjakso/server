package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.apply.dto.PageRes;
import com.gongjakso.server.domain.post.dto.ContestPageRes;
import com.gongjakso.server.domain.post.dto.PostDeleteRes;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.service.ContestService;
import com.gongjakso.server.domain.post.service.PostService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Contest", description = "공모전 페이지 API")
public class ContestController {

    private final ContestService contestService;

    @Operation(summary = "공모전 정보 API", description = "공모전 페이징을 검색, 정렬 조건 등을 이용함")
    @GetMapping("/contestList")
    public ApplicationResponse<ContestPageRes> getApplyList(@RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "6") int size,
                                                            @RequestParam(name = "region", defaultValue = "null") String region,
                                                            @RequestParam(name = "sort", defaultValue = "null") String sort){
        return ApplicationResponse.ok(contestService.contestListPage(page,size,region,sort));
    }
}
