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
import org.springframework.data.domain.Page;
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
    public TeamRes updateTeam(Member member, Long contestId, Long teamId, TeamReq teamReq) {
        // Validation
        contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
        if (!team.getContest().getId().equals(contestId)) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
        if (!team.getMember().getId().equals(member.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        // Business Logic
        team.update(teamReq);
        Team updatedTeam = teamRepository.save(team);

        // Response
        return TeamRes.of(updatedTeam);
    }

    @Transactional
    public void deleteTeam(Member member, Long contestId, Long teamId) {
        // Validation
        contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
        if (!team.getContest().getId().equals(contestId)) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
        if (!team.getMember().getId().equals(member.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        // Business Logic
        teamRepository.delete(team);
    }

    public TeamRes getTeam(Long contestId, Long teamId) {
        // Validation
        contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));

        // Business Logic
        return TeamRes.of(team);
    }

    public Page<TeamRes> getTeamList(Long contestId) {
        // Validation
        contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));

        // Business Logic
        return null;
    }
}
