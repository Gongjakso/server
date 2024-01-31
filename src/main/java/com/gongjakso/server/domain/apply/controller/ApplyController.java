package com.gongjakso.server.domain.apply.controller;

import com.gongjakso.server.domain.apply.dto.ApplyReq;
import com.gongjakso.server.domain.apply.dto.ApplicationRes;
import com.gongjakso.server.domain.apply.dto.ApplyRes;
import com.gongjakso.server.domain.apply.service.ApplyService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/apply")
@Tag(name = "Apply", description = "팀 빌딩 관련 API")
public class ApplyController {
    private final ApplyService applyService;
    //지원 요청 api
    @PostMapping("/{post_id}")
    public ApplicationResponse<Void> addApply(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId, @RequestBody ApplyReq req){
        applyService.save(principalDetails.getMember(),postId,req);
        return ApplicationResponse.created();
    }
    //프로젝트 지원서 요청 api
    @GetMapping("/{post_id}")
    public ApplicationResponse<ApplyRes> getApply(@PathVariable("post_id") Long postId){
       return applyService.findApply(postId);
    }
    //지원서 열람 요청 api
    @PatchMapping("/{apply_id}/open")
    public ApplicationResponse<Void> updateIsOpenStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("apply_id") Long applyId){
        return applyService.updateOpen(applyId);
    }
    //지원서 지원 요청 api
    @PatchMapping("/{apply_id}/recruit")
    public ApplicationResponse<Void> updateIsRecruitStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("apply_id") Long applyId){
        return applyService.updateRecruit(applyId,true);
    }
    //지원서 미선발 요청 api
    @PatchMapping("/{apply_id}/not-recruit")
    public ApplicationResponse<Void> updateNotRecruitStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("apply_id") Long applyId){
        return applyService.updateRecruit(applyId,false);
    }
    // 특정 지원자 지원서 가져오는 api
    @GetMapping("/{apply_id}/application")
    public ApplicationResponse<ApplicationRes> findApplication(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("apply_id") Long applyId){
        return applyService.findApplication(applyId);
    }
}
