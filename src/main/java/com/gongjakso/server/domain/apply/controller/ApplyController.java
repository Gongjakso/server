package com.gongjakso.server.domain.apply.controller;

import com.gongjakso.server.domain.apply.dto.*;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.service.ApplyService;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/apply")
@Tag(name = "Apply", description = "팀 빌딩 관련 API")
public class ApplyController {
    private final ApplyService applyService;
    //지원 요청 api
    @Operation(summary = "공고 지원 API", description = "팀 지원하기 모달창에서 지원 요청")
    @PostMapping("/{post_id}")
    public ApplicationResponse<Void> createApply(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId, @RequestBody ApplyReq req){
        applyService.save(principalDetails.getMember(),postId,req);
        return ApplicationResponse.created();
    }
    //팀 공고 요청 api
    @Operation(summary = "내가 모집 중인 팀 정보 API", description = "내가 모집 중인 팀 페이지에서 필요한 팀 정보 요청")
    @GetMapping("/{post_id}")
    public ApplicationResponse<ApplyRes> getApply(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("post_id") Long postId){
        return ApplicationResponse.ok(applyService.findApply(principalDetails.getMember(),postId));
    }
    //내가 모집 중인 팀 지원자 정보 요청 api
    @Operation(summary = "내가 모집 중인 팀 지원자 정보 API", description = "내가 모집 중인 팀 페이지에서 필요한 지원자 정보 요청")
    @GetMapping("/{post_id}/applylist")
    public ApplicationResponse<ApplyPageRes> getApplyList(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("post_id") Long postId,@RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name = "size", defaultValue = "11") int size){
        return ApplicationResponse.ok(applyService.applyListPage(principalDetails.getMember(),postId,page,size));
    }
    //내가 참여한 공고 정보 요청 api
    @Operation(summary = "내가 참여한 공고 정보 API", description = "내가 참여한 공고 정보")
    @GetMapping("/my-participation-post")
    public ApplicationResponse<ParticipationPageRes> getMyParticipationPostList(@RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name = "size", defaultValue = "6") int size){
        return ApplicationResponse.ok(applyService.myParticipationPostListPage(page,size));
    }
    //지원서 열람 요청 api
    @Operation(summary = "지원서 열람 API", description = "내가 모집 중인 팀 페이지에서 지원서 열람 시")
    @PatchMapping("/{apply_id}/open")
    public ApplicationResponse<Void> updateIsOpenStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("apply_id") Long applyId){
        applyService.updateState(principalDetails.getMember(),applyId, ApplyType.OPEN_APPLY);
        return ApplicationResponse.ok();
    }
    //지원서 지원 요청 api
    @Operation(summary = "합류하기 API", description = "합류하기 버튼 클릭 시")
    @PatchMapping("/{apply_id}/recruit")
    public ApplicationResponse<Void> updateIsRecruitStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("apply_id") Long applyId){
        applyService.updateState(principalDetails.getMember(),applyId,ApplyType.PASS);
        return ApplicationResponse.ok();
    }
    //지원서 미선발 요청 api
    @Operation(summary = "미선발 API", description = "미선발 버튼 클릭 시")
    @PatchMapping("/{apply_id}/not-recruit")
    public ApplicationResponse<Void> updateNotRecruitStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("apply_id") Long applyId){
        applyService.updateState(principalDetails.getMember(),applyId,ApplyType.NOT_PASS);
        return ApplicationResponse.ok();
    }
    // 특정 지원자 지원서 가져오는 api
    @Operation(summary = "지원서 API", description = "내가 모집 중인 팀 페이지에서 지원자 지원서 요청")
    @GetMapping("/{post_id}/{apply_id}/application")
    public ApplicationResponse<ApplicationRes> findApplication(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("apply_id") Long applyId,@PathVariable("post_id") Long postId){
        return ApplicationResponse.ok(applyService.findApplication(principalDetails.getMember(),applyId,postId));
    }
    //공고 카테고리 요청 api
    @Operation(summary = "지원시 뜨는 공고 정보 API", description = "팀 지원하기 모달 창에서 카테고리들(지원 분야) 요청")
    @GetMapping("/{post_id}/category")
    public ApplicationResponse<CategoryRes> getCategory(@PathVariable("post_id") Long postId){
        return ApplicationResponse.ok(applyService.findPostCategory(postId));
    }
    //공고 마감 요청 api
    @Operation(summary = "공고 마감 API", description = "내가 모집 중인 팀 페이지에서 공고 마감 버튼 클릭시")
    @PatchMapping("/{post_id}/close")
    public ApplicationResponse<Void> updatePostStatusToClose(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId){
        applyService.updatePostState(principalDetails.getMember(),postId, PostStatus.CLOSE);
        return ApplicationResponse.ok();
    }
    //공고 취소 요청 api
    @Operation(summary = "공고 취소 API", description = "내가 모집 중인 팀 페이지에서 공고 취소 버튼 클릭시")
    @PatchMapping("/{post_id}/cancel")
    public ApplicationResponse<Void> updatePostStatusToCancel(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId){
        applyService.updatePostState(principalDetails.getMember(),postId, PostStatus.CLOSE);
        return ApplicationResponse.ok();
    }
    //공고 기간 연장 요청 api
    @Operation(summary = "공고 연장 API", description = "내가 모집 중인 팀 페이지에서 공고 연장 버튼 클릭시")
    @PatchMapping("/{post_id}/extension")
    public ApplicationResponse<Void> updatePostPeriod(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId, @RequestBody PeriodReq req){
        applyService.updatePostPeriod(principalDetails.getMember(),postId,req);
        return ApplicationResponse.ok();
    }

    @Operation(summary = "내가 지원한 팀 리스트 API", description = "현재 지원 중인 상태의 팀 정보 반환")
    @GetMapping("/my")
    public ApplicationResponse<List<MyPageRes>> getMyApplyList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApplicationResponse.ok(applyService.getMyApplyList(principalDetails.getMember()));
    }

    @Operation(summary = "지원서 열람 API", description = "해당 공고에 대한 사용자의 지원서 정보 반환")
    @GetMapping("/my/{post_id}")
    public ApplicationResponse<ApplicationRes> getMyApplication(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId){
        return ApplicationResponse.ok(applyService.getMyApplication(principalDetails.getMember(), postId));
    }

    @Operation(summary = "지원 취소 API", description = "해당 공고에 대한 지원을 취소한다.")
    @PatchMapping("/cancel/{apply_id}")
    public ApplicationResponse<PatchApplyRes> cancelApply(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("apply_id") Long applyId){
        return ApplicationResponse.ok(applyService.cancelApply(principalDetails.getMember(), applyId));
    }
}
