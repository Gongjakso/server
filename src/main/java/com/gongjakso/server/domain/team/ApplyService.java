package com.gongjakso.server.domain.team;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.team.dto.ApplyReq;
import com.gongjakso.server.domain.team.dto.ApplyRes;
import com.gongjakso.server.domain.team.entity.Apply;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.repository.ApplyRepository;
import com.gongjakso.server.domain.team.repository.TeamRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final TeamRepository teamRepository;

    public ApplyRes apply(Member member, Long teamId, ApplyReq applyReq) {
        //Validation: member, teamId, 지원 가능 기간인지, 이미 지원했는지 유효성 검사
        if(member == null) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

        if(team.getFinishedAt().isBefore(now())) {
            throw new ApplicationException(ErrorCode.FINISHED_TEAM_APPLY_EXCEPTION);
        }

        if(applyRepository.existsByMemberAndTeam(member.getId(), team.getId())) {
            throw new ApplicationException(ErrorCode.ALREADY_APPLY_EXCEPTION);
        }

        //Business Logic
        Portfolio portfolio = Optional.ofNullable(applyReq.portfolioId())
                .map(portfolioId -> Portfolio.builder().id(portfolioId).build())
                .orElse(null);

        Apply apply = ApplyReq.toEntity(applyReq, team, member, portfolio);

        applyRepository.save(apply);

        //Response
        return ApplyRes.of(apply);
    }
}
