package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.post.dto.GetProjectRes;
import com.gongjakso.server.domain.post.dto.PostDeleteRes;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
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

    @Operation(summary = "공모전/프로젝트 공고 상세 조회 API", description = "공모전/프로젝트 공고 상세 조회")
    @GetMapping("/{id}")
    public ApplicationResponse<PostRes> read(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id) {
        return ApplicationResponse.ok(postService.read(principalDetails.getMember(), id));
    }

    @Operation(summary = "공모전/프로젝트 공고 수정 API", description = "팀빌딩 페이지에서 정보 입력 후 공고 수정")
    @PutMapping("/{id}")
    public ApplicationResponse<PostRes> modify(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id, @RequestBody PostReq req) {
        return ApplicationResponse.ok(postService.modify(principalDetails.getMember(), id, req));
    }

    @Operation(summary = "공모전/프로젝트 공고 삭제 API", description = "내가 모집 중인 팀 페이지에서 공고 삭제")
    @PatchMapping("/{id}")
    public ApplicationResponse<PostDeleteRes> delete(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id){
        return ApplicationResponse.ok(postService.delete(principalDetails.getMember(), id));
    }

    @Operation(summary = "프로젝트 공고 목록 조회 및 페이지네이션 API", description = "프로젝트 공고 페이지에서 공고 목록 조회")
    @GetMapping("/project")
    public ApplicationResponse<Page<GetProjectRes>> list(@PageableDefault(size = 6) Pageable pageable,
                                                         @RequestParam(value = "searchWord", required = false) String searchWord,
                                                         @RequestParam(value = "stackName", required = false) String stackName,
                                                         @RequestParam(value = "meetingArea", required = false) String meetingArea,
                                                         @RequestParam(value = "sort", required = false) String sort) {
        if(stackName.isBlank() && meetingArea.isBlank()){
            if(searchWord.isBlank()){
                return ApplicationResponse.ok(postService.getProjects(sort, pageable));
            }else {
                return ApplicationResponse.ok(postService.getProjectsBySearchWord(sort, searchWord, pageable));
            }
        }else {
            return ApplicationResponse.ok(postService.getProjectsByMeetingAreaAndStackNameAndSearchWord(sort, meetingArea, stackName, searchWord, pageable));
        }
    }
}