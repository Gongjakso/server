package com.gongjakso.server.domain.team.controller;

import com.gongjakso.server.domain.team.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
@Tag(name = "Team", description = "팀 관련 API 리스트")
public class TeamController {

    private final TeamService teamService;
}
