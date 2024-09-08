package com.gongjakso.server.domain.team;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.portfolio.repository.PortfolioRepository;
import com.gongjakso.server.domain.team.dto.ApplyReq;
import com.gongjakso.server.domain.team.dto.ApplyRes;
import com.gongjakso.server.domain.team.dto.StatusReq;
import com.gongjakso.server.domain.team.entity.Apply;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.repository.ApplyRepository;
import com.gongjakso.server.domain.team.repository.TeamRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final PortfolioRepository portfolioRepository;
    private final TeamRepository teamRepository;

    public ApplyRes apply(Member member, Long teamId, ApplyReq applyReq) {
        //Validation: member, teamId, 지원 가능 기간인지, 리더가 지원하는지, 이미 지원했는지 유효성 검사
        if(member == null) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        Team team = teamRepository.findTeamById(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));

        if(team.getFinishedAt().isBefore(ChronoLocalDate.from(now()))) {
            throw new ApplicationException(ErrorCode.APPLY_PERIOD_EXPIRED_EXCEPTION);
        }

        if(team.getMember().equals(member)) {
            throw new ApplicationException(ErrorCode.APPLY_LEADER_NOT_ALLOWED_EXCEPTION);
        }

        if(applyRepository.existsByMemberIdAndTeamIdAndDeletedAtIsNull(member.getId(), teamId)) {
            throw new ApplicationException(ErrorCode.APPLY_ALREADY_EXISTS_EXCEPTION);
        }

        //Business Logic
        Portfolio portfolio = portfolioRepository.findById(applyReq.portfolioId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.PORTFOLIO_NOT_FOUND_EXCEPTION));

        Apply apply = ApplyReq.toEntity(applyReq, team, member, portfolio);

        applyRepository.save(apply);

        //Response
        return ApplyRes.of(apply);
    }

    public Page<ApplyRes> getMyApplies(Member member, Pageable pageable) {
        //Validation: member 조회
        if(member == null) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Business Logic
        Page<ApplyRes> applyPage = applyRepository.findByMemberAndPage(member, pageable);

        //Response
        return applyPage;
    }

    public ApplyRes getApply(Member member, Long applyId) {
        //Validation: Apply가 유효하지 않거나, 리더가 아니거나, 자신이 아닌 경우 예외 처리
        Apply apply = applyRepository.findApplyWithTeam(applyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.APPLY_NOT_FOUND_EXCEPTION));

        if(member == null || !Objects.equals(member.getId(), apply.getMember().getId())
                || !Objects.equals(member.getId(), apply.getTeam().getMember().getId())
                ) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Response
        return ApplyRes.of(apply);
    }

    public ApplyRes selectApply(Member member, Long applyId, StatusReq req){
        //Validation: Apply가 유효하지 않거나, 리더가 아닌 경우 예외 처리
        Apply apply = applyRepository.findApplyWithTeam(applyId).orElseThrow(() -> new ApplicationException(ErrorCode.APPLY_NOT_FOUND_EXCEPTION));

        if(member == null || !Objects.equals( apply.getTeam().getMember().getId(), member.getId())){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Business Logic
        if (req != null) {
            apply.select(req.status());
        }

        applyRepository.save(apply);

        //Response
        return ApplyRes.of(apply);
    }

    public ApplyRes viewApply(Member member, Long applyId) {
        //Validation: Apply가 유효하지 않거나, 리더가 아닌 경우 예외 처리
        Apply apply = applyRepository.findApplyWithTeam(applyId).orElseThrow(() -> new ApplicationException(ErrorCode.APPLY_NOT_FOUND_EXCEPTION));

        if(member==null || !Objects.equals(apply.getTeam().getMember().getId(), member.getId())){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Business Logic
        apply.setViewed();

        applyRepository.save(apply);

        //Response
        return ApplyRes.of(apply);
    }

    public void cancelApply(Member member, Long applyId) {
        //Validation: Apply가 유효하지 않거나, 지원자 아닌 경우 예외 처리
        Apply apply = applyRepository.findByIdAndDeletedAtIsNull(applyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.APPLY_NOT_FOUND_EXCEPTION));

        if(member == null || !Objects.equals(apply.getMember().getId(), member.getId())){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Business Logic
        applyRepository.delete(apply);
    }
}
