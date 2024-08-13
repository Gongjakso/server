package com.gongjakso.server.domain.contest.controller;

import com.gongjakso.server.domain.contest.dto.request.ContestReq;
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
    @Operation(summary = "공모전 생성 API", description = "")
    @PostMapping("")
    public ApplicationResponse<Void> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestPart(required = false) MultipartFile image, @Valid @RequestPart ContestReq contestReq){
        contestService.save(image,contestReq);
        return ApplicationResponse.created();
    }
    @GetMapping("/{contest_id}")
    public ApplicationResponse<ContestRes> findContest(@PathVariable Long contest_id){
        return ApplicationResponse.ok(contestService.find(contest_id));
    }
}
