package com.gongjakso.server.domain.team.service;

import com.gongjakso.server.domain.team.dto.response.TeamRes;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamRes createTeam(Long contestId) {
        // Validation
        // TODO: Contest Validation 코드 추가 필요

        // Business Logic

        // Response
        return null;
    }

    @Transactional
    public void deleteTeam(Long contestId, Long teamId) {
        // Validation
        // TODO: Contest Validation 코드 추가 필요
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("해당 팀이 존재하지 않습니다."));

        // Business Logic
        teamRepository.delete(team);
    }


}
