package com.gongjakso.server.domain.team.controller;

import com.gongjakso.server.domain.team.service.TeamService;
import com.gongjakso.server.global.common.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/contest/{contest_id}/team")
@Tag(name = "Team", description = "팀 관련 API 리스트")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성 API", description = "특정 공모전에 해당하는 팀을 생성하는 API")
    @PostMapping("/create")
    public ApplicationResponse<?> createTeam(@PathVariable(value = "contest_id") Long contestId) {
        return null;
    }

    @Operation(summary = "팀 수정 API", description = "특정 공모전에 해당하는 팀을 수정하는 API")
    @PutMapping("/update/{team_id}")
    public ApplicationResponse<?> updateTeam(@PathVariable(value = "contest_id") Long contestId,
                                             @PathVariable(value = "team_id") Long teamId) {
        return null;
    }

    @Operation(summary = "팀 삭제 API", description = "특정 공모전에 해당하는 팀을 삭제하는 API (논리적 삭제)")
    @PutMapping("/delete/{team_id}")
    public ApplicationResponse<?> deleteTeam(@PathVariable(value = "contest_id") Long contestId,
                                             @PathVariable(value = "team_id") Long teamId) {
        return null;
    }

    @Operation(summary = "팀 조회 API", description = "특정 공모전에 해당하는 팀을 조회하는 API")
    @GetMapping("/{team_id}")
    public ApplicationResponse<?> getTeam(@PathVariable(value = "contest_id") Long contestId,
                                          @PathVariable(value = "team_id") Long teamId) {
        return null;
    }

    @Operation(summary = "팀 리스트 조회 API", description = "특정 공모전에 해당하는 팀 리스트를 조회하는 API (별도의")
    @GetMapping("/list")
    public ApplicationResponse<?> getTeamList(@PathVariable(value = "contest_id") Long contestId) {
        return null;
    }
}
