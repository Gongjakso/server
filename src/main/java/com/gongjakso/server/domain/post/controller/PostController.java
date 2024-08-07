package com.gongjakso.server.domain.post.controller;

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

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "공고 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "공모전/프로젝트 공고 생성 API", description = "팀빌딩 페이지에서 정보 입력 후 공고 생성")
    @PostMapping("")
    public ApplicationResponse<PostRes> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PostReq req) {
        return ApplicationResponse.ok(postService.create(principalDetails.getMember(), req));
    }

    @Operation(summary = "사용자 별 상세 조회 API", description = "사용자별로 공고 상세 조회 다르게 반환")
    @GetMapping("/{id}")
    public ApplicationResponse<?> read(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long postId) {
        return ApplicationResponse.ok(postService.read(principalDetails, postId));
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
        return ApplicationResponse.ok(postService.getContestsByFilter(sort, meetingCity, meetingTown, category, searchWord, pageable));
    }

    @Operation(summary = "프로젝트 공고 목록 조회 및 페이지네이션 API", description = "프로젝트 공고 페이지에서 공고 목록 조회")
    @GetMapping("/project")
    public ApplicationResponse<Page<GetProjectRes>> projectList(@PageableDefault(size = 6) Pageable pageable,
                                                         @RequestParam(value = "searchWord", required = false) String searchWord,
                                                         @RequestParam(value = "stackName", required = false) String stackName,
                                                                @RequestParam(value = "meetingCity", required = false) String meetingCity,
                                                                @RequestParam(value = "meetingTown", required = false) String meetingTown,
                                                         @RequestParam(value = "sort", required = false) String sort) {
        return ApplicationResponse.ok(postService.getProjectsByFilter(sort, meetingCity, meetingTown, stackName, searchWord, pageable));
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

    @Operation(summary = "내가 모집 중인 팀 조회 API", description = "프로젝트/공모전 별 각 한 개씩 묶어서 리스트 형태로 반환")
    @GetMapping("/my")
    public ApplicationResponse<List<MyPageRes>> getMyPostList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApplicationResponse.ok(postService.getMyPostList(principalDetails.getMember()));
    }

    @Operation(summary = "공고와 관련된 유저인지를 확인하는 API", description = "팀장이거나, 신청자인 경우를 확인해서 결과로 반환")
    @GetMapping("/check/{post_id}")
    public ApplicationResponse<GetPostRelation> checkPostRelation(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId) {
        return ApplicationResponse.ok(postService.checkPostRelation(principalDetails.getMember(), postId));
    }

    @Operation(summary = "내가 스크랩한 프로젝트 공고 조회 API", description = "스크랩한 프로젝트 공고 페이지로 반환")
    @GetMapping("/project/myScrap")
    public ApplicationResponse<Page<GetProjectRes>> MyScrapProjectList(@PageableDefault(size = 6) Pageable pageable,@AuthenticationPrincipal PrincipalDetails principalDetails){
        return ApplicationResponse.ok(postService.getMyScrapProject(principalDetails.getMember(), pageable));
    }

    @Operation(summary = "내가 스크랩한 공모전 공고 조회 API", description = "스크랩한 공모전 공고 페이지로 반환")
    @GetMapping("/contest/myScrap")
    public ApplicationResponse<Page<GetContestRes>> MyScrapContestList(@PageableDefault(size = 6) Pageable pageable,@AuthenticationPrincipal PrincipalDetails principalDetails){
        return ApplicationResponse.ok(postService.getMyScrapContest(principalDetails.getMember(), pageable));
    }

    @Operation(summary = "활동 종료 API", description = "팀장이 활동 중인 프로젝트 공고를 활동 종료할 때 사용하는 API로, 활동 종료된 공고의 정보가 반환")
    @PatchMapping("/complete/{post_id}")
    public ApplicationResponse<PostSimpleRes> completePost(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId) {
        return ApplicationResponse.ok(postService.completePost(principalDetails.getMember(), postId));
    }
}