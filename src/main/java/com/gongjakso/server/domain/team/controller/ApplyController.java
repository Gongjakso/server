package com.gongjakso.server.domain.team.controller;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.domain.team.ApplyService;
import com.gongjakso.server.domain.team.dto.ApplyReq;
import com.gongjakso.server.domain.team.dto.ApplyRes;
import com.gongjakso.server.domain.team.dto.StatusReq;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v2/apply")
@RequiredArgsConstructor
@Tag(name = "지원 관련 API", description = "지원 관련 API")
public class ApplyController {

    private final ApplyService applyService;
    private final MemberRepository memberRepository;

    @Operation(summary = "지원하기 ", description = "팀에 지원하는 API")
    @PostMapping("/{team_id}")
    public ApplicationResponse<ApplyRes> apply(@PathVariable("team_id") Long teamId, @Valid @RequestBody ApplyReq req) {
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        return ApplicationResponse.ok(applyService.apply(member, teamId, req));
    }

    @Operation(summary = "내가 지원한 팀 조회", description = "내가 지원한 팀을 페이징 조회하는 API")
    @GetMapping("/my")
    public ApplicationResponse<Page<ApplyRes>> getMyApplies(@RequestParam Long page) {
        Pageable pageable = PageRequest.of(page.intValue(), 6);
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        return ApplicationResponse.ok(applyService.getMyApplies(member, pageable));
    }

    @Operation(summary = "특정 지원자 지원서 가져오기", description = "특정 지원자의 지원서를 가져오는 API")
    @GetMapping("/{apply_id}")
    public ApplicationResponse<ApplyRes> getApply(@PathVariable("apply_id") Long applyId) {
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        return ApplicationResponse.ok(applyService.getApply(member, applyId));
    }

    @Operation(summary = "지원서 선발/미선발", description = "지원자를 선발/미선발하는 API")
    @PatchMapping("/select/{apply_id}")
    public ApplicationResponse<ApplyRes> selectApply(@PathVariable("apply_id") Long applyId, @RequestBody StatusReq req) {
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        return ApplicationResponse.ok(applyService.selectApply(member, applyId, req));
    }

    @Operation(summary = "지원서 열람", description = "지원서 열람 열람 여부 관리 API")
    @PatchMapping("/view/{apply_id}")
    public ApplicationResponse<ApplyRes> viewApply(@PathVariable("apply_id") Long applyId) {
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        return ApplicationResponse.ok(applyService.viewApply(member, applyId));
    }

    @Operation(summary = "지원 취소", description = "지원자가 지원 취소하는 API")
    @DeleteMapping("/{apply_id}")
    public ApplicationResponse<Void> cancelApply(@PathVariable("apply_id") Long applyId) {
        Member member = memberRepository.findById(2L)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        applyService.cancelApply(member, applyId);
        return ApplicationResponse.ok();
    }
}
