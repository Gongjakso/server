package com.gongjakso.server.domain.team.controller;

import com.gongjakso.server.domain.team.dto.request.TeamReq;
import com.gongjakso.server.domain.team.service.TeamService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
@Tag(name = "Team", description = "팀 관련 API 리스트")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성 API", description = "특정 공모전에 해당하는 팀을 생성하는 API")
    @PostMapping("/contest/{contest_id}/team/create")
    public ApplicationResponse<?> createTeam(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable(value = "contest_id") Long contestId, @Valid @RequestBody TeamReq teamReq) {
        return ApplicationResponse.created(teamService.createTeam(principalDetails.getMember(), contestId, teamReq));
    }

    @Operation(summary = "팀 수정 API", description = "특정 공모전에 해당하는 팀을 수정하는 API")
    @PutMapping("/contest/{contest_id}/team/{team_id}/update")
    public ApplicationResponse<?> updateTeam(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable(value = "contest_id") Long contestId,
                                             @PathVariable(value = "team_id") Long teamId,
                                             @Valid @RequestBody TeamReq teamReq) {
        return ApplicationResponse.ok(teamService.updateTeam(principalDetails.getMember(), contestId, teamId, teamReq));
    }

    @Operation(summary = "팀 모집 연장하기 API", description = "특정 공모전에 해당하는 팀의 모집 마감일을 연장하는 API")
    @PatchMapping("/contest/{contest_id}/team/{team_id}/extend-recruit")
    public ApplicationResponse<?> extendRecruit(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @PathVariable(value = "contest_id") Long contestId,
                                                @PathVariable(value = "team_id") Long teamId,
                                                @RequestParam(value = "extend-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate extendDate) {
        return ApplicationResponse.ok(teamService.extendRecruit(principalDetails.getMember(), contestId, teamId, extendDate));
    }

    @Operation(summary = "팀 모집 마감하기 API", description = "특정 공모전에 해당하는 팀의 모집을 마감하는 API")
    @PatchMapping("/contest/{contest_id}/team/{team_id}/close-recruit")
    public ApplicationResponse<?> closeRecruit(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                               @PathVariable(value = "contest_id") Long contestId,
                                               @PathVariable(value = "team_id") Long teamId) {
        return ApplicationResponse.ok(teamService.closeRecruit(principalDetails.getMember(), contestId, teamId));
    }

    @Operation(summary = "팀 모집 취소하기 API", description = "특정 공모전에 해당하는 팀의 모집을 취소하는 API")
    @PatchMapping("/contest/{contest_id}/team/{team_id}/cancel-recruit")
    public ApplicationResponse<?> cancelRecruit(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @PathVariable(value = "contest_id") Long contestId,
                                                @PathVariable(value = "team_id") Long teamId) {
        return ApplicationResponse.ok(teamService.cancelRecruit(principalDetails.getMember(), contestId, teamId));
    }

    @Operation(summary = "팀 삭제 API", description = "특정 공모전에 해당하는 팀을 삭제하는 API (논리적 삭제)")
    @DeleteMapping("/contest/{contest_id}/team/{team_id}/delete")
    public ApplicationResponse<?> deleteTeam(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable(value = "contest_id") Long contestId,
                                             @PathVariable(value = "team_id") Long teamId) {
        teamService.deleteTeam(principalDetails.getMember(), contestId, teamId);
        return ApplicationResponse.ok();
    }

    @Operation(summary = "팀 조회 API", description = "특정 공모전에 해당하는 팀을 조회하는 API")
    @GetMapping("/contest/{contest_id}/team/{team_id}")
    public ApplicationResponse<?> getTeam(@PathVariable(value = "contest_id") Long contestId,
                                          @PathVariable(value = "team_id") Long teamId) {
        return ApplicationResponse.ok(teamService.getTeam(contestId, teamId));
    }

    @Operation(summary = "팀 리스트 조회 API", description = "특정 공모전에 해당하는 팀 리스트를 조회하는 API (오프셋 기반 페이지네이션)")
    @GetMapping("/contest/{contest_id}/team/list")
    public ApplicationResponse<?> getTeamList(@PathVariable(value = "contest_id") Long contestId,
                                              @RequestParam(value = "province", required = false) String province,
                                              @RequestParam(value = "district", required = false) String district,
                                              @PageableDefault(size = 8) Pageable pageable) {
        return ApplicationResponse.ok(teamService.getTeamListWithContest(contestId, province, district, pageable));
    }

    @Operation(summary = "팀 리스트 조회 API", description = "공모전에 상관없이 팀 리스트를 조회하는 API (검색 기능 존재 / 오프셋 기반 페이지네이션)")
    @GetMapping("/team/list")
    public ApplicationResponse<?> getTeamList(@RequestParam(value = "province", required = false) String province,
                                              @RequestParam(value = "district", required = false) String district,
                                              @RequestParam(value = "keyword", required = false) String keyword,
                                              @PageableDefault(size = 8) Pageable pageable) {
        return ApplicationResponse.ok(teamService.getTeamListWithoutContest(province, district, keyword, pageable));
    }

    @Operation(summary = "내가 모집 중인 팀 리스트 조회 API", description = "공모전에 상관없이 내가 모집 중인 팀 리스트를 조회하는 API (오프셋 기반 페이지네이션)")
    @GetMapping("/team/my-recruit")
    public ApplicationResponse<?> getMyRecruitTeamList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                      @PageableDefault(size = 8) Pageable pageable) {
        return ApplicationResponse.ok(teamService.getMyRecruitTeamList(principalDetails.getMember(), pageable));
    }

    @Operation(summary = "내가 참여한 팀 리스트 조회 API", description = "공모전에 상관없이 내가 참여한 팀 리스트를 조회하는 API (오프셋 기반 페이지네이션)")
    @GetMapping("/team/my-apply")
    public ApplicationResponse<?> getMyApplyTeamList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                    @PageableDefault(size = 8) Pageable pageable) {
        return ApplicationResponse.ok(teamService.getMyApplyTeamList(principalDetails.getMember(), pageable));
    }

    @Operation(summary = "특정 팀 스크랩하기", description = "특정 팀을 스크랩하는 API")
    @PostMapping("/team/{team_id}/scrap")
    public ApplicationResponse<?> scrapTeam(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @PathVariable(value = "team_id") Long teamId) {
        teamService.scrapTeam(principalDetails.getMember(), teamId);
        return ApplicationResponse.ok();
    }

    @Operation(summary = "특정 팀 스크랩 취소하기", description = "특정 팀 스크랩을 취소하는 API")
    @DeleteMapping("/team/{team_id}/scrap")
    public ApplicationResponse<?> cancelScrapTeam(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                 @PathVariable(value = "team_id") Long teamId) {
        teamService.cancelScrapTeam(principalDetails.getMember(), teamId);
        return ApplicationResponse.ok();
    }

    @Operation(summary = "스크랩한 팀 리스트 조회 API", description = "스크랩한 팀 리스트를 조회하는 API (오프셋 기반 페이지네이션)")
    @GetMapping("/team/scrap/list")
    public ApplicationResponse<?> getScrapTeamList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                  @PageableDefault(size = 8) Pageable pageable) {
        return ApplicationResponse.ok(teamService.getScrapTeamList(principalDetails.getMember(), pageable));
    }
}