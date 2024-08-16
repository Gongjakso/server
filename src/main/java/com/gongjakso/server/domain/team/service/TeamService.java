package com.gongjakso.server.domain.team.service;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.team.dto.response.TeamRes;
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

}
