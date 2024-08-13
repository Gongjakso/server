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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/contest")
public class ContestController {
    private final ContestService contestService;
    @Operation(summary = "공모전 생성 API", description = "")
    @PostMapping("")
    public ApplicationResponse<Void> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody ContestReq contestReq){
        contestService.save(contestReq);
        return ApplicationResponse.created();
    }
    @GetMapping("/{contest_id}")
    public ApplicationResponse<ContestRes> findContest(@PathVariable Long contest_id){
        return ApplicationResponse.ok(contestService.find(contest_id));
    }
}
