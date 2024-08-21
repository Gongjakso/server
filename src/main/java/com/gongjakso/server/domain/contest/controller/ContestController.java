package com.gongjakso.server.domain.contest.controller;

import com.gongjakso.server.domain.contest.dto.request.ContestReq;
import com.gongjakso.server.domain.contest.dto.request.UpdateContestDto;
import com.gongjakso.server.domain.contest.dto.response.ContestListRes;
import com.gongjakso.server.domain.contest.dto.response.ContestRes;
import com.gongjakso.server.domain.contest.service.ContestService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/contest")
public class ContestController {
    private final ContestService contestService;
    @Operation(description = "공모전 생성 API - 관리자만")
    @PostMapping("")
    public ApplicationResponse<Void> create(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestPart(name = "image",required = false) MultipartFile image,
                                            @Valid @RequestPart(name = "contestReq") ContestReq contestReq){
        contestService.save(principalDetails.getMember(),image,contestReq);
        return ApplicationResponse.created();
    }

    @Operation(description = "공모전 정보 API")
    @GetMapping("/{contest_id}")
    public ApplicationResponse<ContestRes> find(@PathVariable Long contest_id, HttpServletRequest request, HttpServletResponse response){
        return ApplicationResponse.ok(contestService.find(contest_id,request,response));
    }

    @Operation(description = "공모전 검색 API")
    @GetMapping("/search")
    public ApplicationResponse<ContestListRes> search(
            @RequestParam(name = "word", defaultValue = "공모전") String word,
            @RequestParam(name = "sortAt", defaultValue = "createdAt") String sortAt,
            @PageableDefault(size = 12,page = 0) Pageable pageable){
        return ApplicationResponse.ok(contestService.search(word,sortAt,pageable));
    }

    @Operation(description = "공모전 수정 API - 관리자만")
    @PatchMapping("/{contest_id}")
    public ApplicationResponse<ContestRes> update(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable Long contest_id,@RequestPart(required = false) MultipartFile image,@Valid @RequestPart UpdateContestDto contestReq){
        return ApplicationResponse.ok(contestService.update(principalDetails.getMember(),contest_id,image,contestReq));
    }
    @Operation(description = "공모전 삭제 API - 관리자만")
    @DeleteMapping("/{contest_id}")
    public ApplicationResponse<Void> delete(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable Long contest_id){
        contestService.delete(principalDetails.getMember(),contest_id);
        return ApplicationResponse.ok();
    }


}
