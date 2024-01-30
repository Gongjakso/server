package com.gongjakso.server.domain.apply.controller;

import com.gongjakso.server.domain.apply.dto.AddApplyReq;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
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
    private final ApplyRepository applyRepository;
    //지원 요청 api
    @PostMapping("/{post_id}")
    public ApplicationResponse<Void> addApply(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId, @RequestBody AddApplyReq req){
        applyService.save(principalDetails.getMember(),postId,req);
        return ApplicationResponse.created();
    }
    @PatchMapping("/{apply_id}/open")
    public ApplicationResponse<Void> updateIsOpenStatus(@PathVariable("apply_id") Long applyId){
        return applyService.updateOpen(applyId);
    }
    @PatchMapping("/{apply_id}/recruit")
    public ApplicationResponse<Void> updateIsRecruitStatus(@PathVariable("apply_id") Long applyId){
        return applyService.updateRecruit(applyId);
    }
//    특정 지원자 지원서 가져오는 api
//    @GetMapping("/apply/{post_id}/application")
//    public ResponseEntity<ApplyMemberRes> findMemberApplication(@RequestHeader Long member_id){
//        ApplyMemberRes applyMemberRes = applyService.findMemberApplication(member_id);
//        return ResponseEntity.ok().body(applyMemberRes);
//    }
}
