package com.gongjakso.server.domain.team.controller;

import com.gongjakso.server.domain.team.ApplyService;
import com.gongjakso.server.domain.team.dto.ApplyReq;
import com.gongjakso.server.domain.team.dto.ApplyRes;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/apply")
@RequiredArgsConstructor
@Tag(name = "지원 관련 API", description = "지원 관련 API")
public class ApplyController {

    private final ApplyService applyService;

    @Operation(summary = "지원하기", description = "팀에 지원하는 API")
    @PostMapping("/{team_id}")
    public ApplicationResponse<ApplyRes> apply(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("team_id") Long teamId, @Valid @RequestBody ApplyReq req) {
        return ApplicationResponse.ok(applyService.apply(principalDetails.getMember(), teamId, req));
    }

}
