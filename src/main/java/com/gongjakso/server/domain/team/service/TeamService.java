package com.gongjakso.server.domain.team.service;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.dto.request.TeamReq;
import com.gongjakso.server.domain.team.dto.response.TeamRes;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.repository.TeamRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;

    @Transactional
    public TeamRes createTeam(Member member, Long contestId, TeamReq teamReq) {
        // Validation
        Contest contest = contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));

        // Business Logic
        Team team = teamReq.from(member, contest);
        Team savedTeam = teamRepository.save(team);

        // Response
        return TeamRes.of(savedTeam);
    }

    @Transactional
    public void deleteTeam(Long contestId, Long teamId) {
        // Validation
        contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));

        // Business Logic
        teamRepository.delete(team);
    }


}
