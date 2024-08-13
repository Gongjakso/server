package com.gongjakso.server.domain.contest.controller;

import com.gongjakso.server.domain.contest.dto.request.ContestReq;
import com.gongjakso.server.domain.contest.dto.request.UpdateContestDto;
import com.gongjakso.server.domain.contest.dto.response.ContestRes;
import com.gongjakso.server.domain.contest.service.ContestService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ApplicationResponse<Void> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestPart(required = false) MultipartFile image, @Valid @RequestPart ContestReq contestReq){
        contestService.save(image,contestReq);
        return ApplicationResponse.created();
    }
    @GetMapping("/{contest_id}")
    public ApplicationResponse<ContestRes> find(@PathVariable Long contest_id){
        return ApplicationResponse.ok(contestService.find(contest_id));
    }
    @Operation(description = "공모전 수정 API - 관리자만")
    @PatchMapping("/{contest_id}")
    public ApplicationResponse<ContestRes> update(@PathVariable Long contest_id,@RequestPart(required = false) MultipartFile image,@Valid @RequestPart UpdateContestDto contestReq){
        return ApplicationResponse.ok(contestService.update(contest_id,image,contestReq));
    }
    @Operation(description = "공모전 삭제 API - 관리자만")
    @DeleteMapping("/{contest_id}")
    public ApplicationResponse<Void> delete(@PathVariable Long contest_id){
        contestService.delete(contest_id);
        return ApplicationResponse.ok();
    }
}
