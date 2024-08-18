package com.gongjakso.server.domain.team.controller;

import com.gongjakso.server.domain.team.dto.request.TeamReq;
import com.gongjakso.server.domain.team.service.TeamService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/contest/{contest_id}/team")
@Tag(name = "Team", description = "팀 관련 API 리스트")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성 API", description = "특정 공모전에 해당하는 팀을 생성하는 API")
    @PostMapping("/create")
    public ApplicationResponse<?> createTeam(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable(value = "contest_id") Long contestId, @Valid @RequestBody TeamReq teamReq) {
        return ApplicationResponse.created(teamService.createTeam(principalDetails.getMember(), contestId, teamReq));
    }

    @Operation(summary = "팀 수정 API", description = "특정 공모전에 해당하는 팀을 수정하는 API")
    @PutMapping("/update/{team_id}")
    public ApplicationResponse<?> updateTeam(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable(value = "contest_id") Long contestId,
                                             @PathVariable(value = "team_id") Long teamId,
                                             @Valid @RequestBody TeamReq teamReq) {
        return ApplicationResponse.ok(teamService.updateTeam(principalDetails.getMember(), contestId, teamId, teamReq));
    }

    @Operation(summary = "팀 삭제 API", description = "특정 공모전에 해당하는 팀을 삭제하는 API (논리적 삭제)")
    @PutMapping("/delete/{team_id}")
    public ApplicationResponse<?> deleteTeam(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable(value = "contest_id") Long contestId,
                                             @PathVariable(value = "team_id") Long teamId) {
        teamService.deleteTeam(principalDetails.getMember(), contestId, teamId);
        return ApplicationResponse.ok();
    }

    @Operation(summary = "팀 조회 API", description = "특정 공모전에 해당하는 팀을 조회하는 API")
    @GetMapping("/{team_id}")
    public ApplicationResponse<?> getTeam(@PathVariable(value = "contest_id") Long contestId,
                                          @PathVariable(value = "team_id") Long teamId) {
        return ApplicationResponse.ok(teamService.getTeam(contestId, teamId));
    }

    @Operation(summary = "팀 리스트 조회 API", description = "특정 공모전에 해당하는 팀 리스트를 조회하는 API (별도의")
    @GetMapping("/list")
    public ApplicationResponse<?> getTeamList(@PathVariable(value = "contest_id") Long contestId,
                                              @RequestParam(value = "province", required = false) String province,
                                                @RequestParam(value = "district", required = false) String district,
                                              @PageableDefault(size = 8) Pageable pageable) {
        return ApplicationResponse.ok(teamService.getTeamList(contestId));
    }
}
