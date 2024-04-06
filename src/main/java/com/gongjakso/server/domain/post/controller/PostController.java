package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.apply.service.ApplyService;
import com.gongjakso.server.domain.post.dto.*;
import com.gongjakso.server.domain.post.service.PostService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "공고 관련 API")
public class PostController {

    private final PostService postService;
    private final ApplyService applyService;

    @Operation(summary = "공모전/프로젝트 공고 생성 API", description = "팀빌딩 페이지에서 정보 입력 후 공고 생성")
    @PostMapping("")
    public ApplicationResponse<PostRes> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PostReq req) {
        return ApplicationResponse.ok(postService.create(principalDetails.getMember(), req));
    }

    @Operation(summary = "사용자 구분 API", description = "사용자 구분 API")
    @GetMapping("/{id}")
    public ApplicationResponse<?> divideUser(@AuthenticationPrincipal Optional<PrincipalDetails> principalDetailsOptional, @PathVariable("id") Long id) {
        if(principalDetailsOptional != null && principalDetailsOptional.isPresent()) {
            PrincipalDetails principalDetails = principalDetailsOptional.orElse(null);
            //지원자인지 판단
            if(applyService.getApplicantApplyId(principalDetails.getMember().getMemberId(), id)!= null){
                return applicantView(principalDetails, id);
            }else{
                //팀장인지 판단
                if(postService.isLeader(principalDetails.getMember().getMemberId(), id)){
                    return LeaderView(principalDetails, id);
                }else {
                    return generalView(id);
                }
            }
        } else{
            return generalView(id);
        }
    }

    @Operation(summary = "팀장의 공모전/프로젝트 공고 상세 조회 API", description = "팀장의 공모전/프로젝트 공고 상세 조회")
    public ApplicationResponse<ParticipationPostDetailRes> LeaderView(PrincipalDetails principalDetails, Long id) {
        return ApplicationResponse.ok(postService.participationView("LEADER", principalDetails, id));
    }

    @Operation(summary = "지원자의 공모전/프로젝트 공고 상세 조회 API", description = "지원자의 공모전/프로젝트 공고 상세 조회")
    public ApplicationResponse<ParticipationPostDetailRes> applicantView(PrincipalDetails principalDetails, Long id) {
        return ApplicationResponse.ok(postService.participationView("APPLICANT", principalDetails, id));
    }

    @Operation(summary = "일반사용자의 공모전/프로젝트 공고 상세 조회 API", description = "일반사용자의 공모전/프로젝트 공고 상세 조회")
    public ApplicationResponse<PostDetailRes> generalView(Long id) {
        return ApplicationResponse.ok(postService.generalView(id));
    }

    @Operation(summary = "공모전/프로젝트 공고 수정 API", description = "팀빌딩 페이지에서 정보 입력 후 공고 수정")
    @PutMapping("/{id}")
    public ApplicationResponse<PostRes> modify(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id, @RequestBody PostModifyReq req) {
        return ApplicationResponse.ok(postService.modify(principalDetails.getMember(), id, req));
    }

    @Operation(summary = "공모전/프로젝트 공고 삭제 API", description = "내가 모집 중인 팀 페이지에서 공고 삭제")
    @PatchMapping("/{id}")
    public ApplicationResponse<PostDeleteRes> delete(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id){
        return ApplicationResponse.ok(postService.delete(principalDetails.getMember(), id));
    }

    @Operation(summary = "공모전 공고 목록 조회 및 페이지네이션 API", description = "공모전 공고 페이지에서 공고 목록 조회")
    @GetMapping("/contest")
    public ApplicationResponse<Page<GetContestRes>> contestList(@PageableDefault(size = 6) Pageable pageable,
                                                         @RequestParam(value = "searchWord", required = false) String searchWord,
                                                         @RequestParam(value = "category", required = false) String category,
                                                         @RequestParam(value = "meetingCity", required = false) String meetingCity,
                                                         @RequestParam(value = "meetingTown", required = false) String meetingTown,
                                                         @RequestParam(value = "sort", required = false) String sort) {
        if(category.isBlank() && meetingCity.isBlank()){
            if(searchWord.isBlank()){
                return ApplicationResponse.ok(postService.getContests(sort, pageable));
            }else {
                return ApplicationResponse.ok(postService.getContestsBySearchWord(sort, searchWord, pageable));
            }
        }else {
            return ApplicationResponse.ok(postService.getContestsByMeetingAreaAndCategoryAndSearchWord(sort, meetingCity, meetingTown, category, searchWord, pageable));
        }
    }

    @Operation(summary = "프로젝트 공고 목록 조회 및 페이지네이션 API", description = "프로젝트 공고 페이지에서 공고 목록 조회")
    @GetMapping("/project")
    public ApplicationResponse<Page<GetProjectRes>> projectList(@PageableDefault(size = 6) Pageable pageable,
                                                         @RequestParam(value = "searchWord", required = false) String searchWord,
                                                         @RequestParam(value = "stackName", required = false) String stackName,
                                                                @RequestParam(value = "meetingCity", required = false) String meetingCity,
                                                                @RequestParam(value = "meetingTown", required = false) String meetingTown,
                                                         @RequestParam(value = "sort", required = false) String sort) {
        if(stackName.isBlank() && meetingCity.isBlank()){
            if(searchWord.isBlank()){
                return ApplicationResponse.ok(postService.getProjects(sort, pageable));
            }else {
                return ApplicationResponse.ok(postService.getProjectsBySearchWord(sort, searchWord, pageable));
            }
        }else {
            return ApplicationResponse.ok(postService.getProjectsByMeetingAreaAndStackNameAndSearchWord(sort, meetingCity, meetingTown, stackName, searchWord, pageable));
        }
    }

    @Operation(summary = "프로젝트 공고 스크랩 기능", description = "프로젝트 공고 스크랩")
    @PostMapping("/{id}")
    public ApplicationResponse<PostScrapRes> scrapPost(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id) {
        return ApplicationResponse.ok(postService.scrapPost(principalDetails.getMember(), id));
    }

    @Operation(summary = "프로젝트 공고 스크랩 조회 기능", description = "프로젝트 공고 스크랩 조회")
    @GetMapping("/scrap/{id}")
    public ApplicationResponse<PostScrapRes> scrapGet(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id) {
        return ApplicationResponse.ok(postService.scrapGet(principalDetails.getMember(), id));
    }

    @Operation(summary = "내가 스크랩한 공고 조회 API", description = "프로젝트/공모전 별 각 한 개씩 묶어서 리스트 형태로 반환")
    @GetMapping("/my")
    public ApplicationResponse<List<MyPageRes>> getMyPostList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApplicationResponse.ok(postService.getMyPostList(principalDetails.getMember()));
    }
}