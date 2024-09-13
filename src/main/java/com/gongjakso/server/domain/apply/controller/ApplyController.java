package com.gongjakso.server.domain.apply.controller;

import com.gongjakso.server.domain.apply.service.ApplyService;
import com.gongjakso.server.domain.apply.dto.request.ApplyReq;
import com.gongjakso.server.domain.apply.dto.response.ApplyRes;
import com.gongjakso.server.domain.team.dto.StatusReq;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v2/apply")
@RequiredArgsConstructor
@Tag(name = "Apply", description = "지원 관련 API")
public class ApplyController {

    private final ApplyService applyService;

    @Operation(summary = "지원하기", description = "팀에 지원하는 API")
    @PostMapping("/{team_id}")
    public ApplicationResponse<ApplyRes> apply(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                               @PathVariable("team_id") Long teamId,
                                               @Valid @RequestBody ApplyReq req) {
        return ApplicationResponse.ok(applyService.apply(principalDetails.getMember(), teamId, req));
    }

    @Operation(summary = "내가 지원한 팀 조회", description = "내가 지원한 팀을 페이징 조회하는 API")
    @GetMapping("/my")
    public ApplicationResponse<Page<ApplyRes>> getMyApplies(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                            @RequestParam Long page) {
        Pageable pageable = PageRequest.of(page.intValue(), 6);
        return ApplicationResponse.ok(applyService.getMyApplies(principalDetails.getMember(), pageable));
    }

    @Operation(summary = "특정 지원자 지원서 가져오기", description = "특정 지원자의 지원서를 가져오는 API")
    @GetMapping("/{apply_id}")
    public ApplicationResponse<ApplyRes> getApply(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                  @PathVariable("apply_id") Long applyId) {
        return ApplicationResponse.ok(applyService.getApply(principalDetails.getMember(), applyId));
    }

    @Operation(summary = "지원서 선발/미선발", description = "지원자를 선발/미선발하는 API")
    @PatchMapping("/select/{apply_id}")
    public ApplicationResponse<ApplyRes> selectApply(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @PathVariable("apply_id") Long applyId,
                                                     @Valid @RequestBody StatusReq req) {
        return ApplicationResponse.ok(applyService.selectApply(principalDetails.getMember(), applyId, req));
    }

    @Operation(summary = "지원서 열람", description = "지원서 열람 열람 여부 관리 API")
    @PatchMapping("/view/{apply_id}")
    public ApplicationResponse<ApplyRes> viewApply(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                   @PathVariable("apply_id") Long applyId) {
        return ApplicationResponse.ok(applyService.viewApply(principalDetails.getMember(), applyId));
    }

    @Operation(summary = "지원 취소", description = "지원자가 지원 취소하는 API")
    @DeleteMapping("/{apply_id}")
    public ApplicationResponse<Void> cancelApply(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                 @PathVariable("apply_id") Long applyId) {
        applyService.cancelApply(principalDetails.getMember(), applyId);
        return ApplicationResponse.ok();
    }
}
