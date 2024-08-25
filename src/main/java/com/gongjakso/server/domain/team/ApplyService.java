package com.gongjakso.server.domain.team;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
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

import java.util.Objects;
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

        if(team.getLeader().equals(member)) {
            throw new ApplicationException(ErrorCode.LEADER_APPLY_EXCEPTION);
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

    public Page<ApplyRes> getMyApplies(Member member, Pageable pageable) {
        //Validation: member 조회
        if(member == null) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Business Logic
        Page<Apply> apply = applyRepository.findByMemberAndPage(member, pageable);

        //Response
        return ApplyRes.toPagination(apply);
    }

    public ApplyRes getApply(Member member, Long applyId) {
        //Validation: member나 Apply가 유효하지 않거나, 리더가 아니거나, 자신이 아닌 경우 예외 처리
        Apply apply = applyRepository.findById(applyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));

        if(member == null || !Objects.equals(member.getId(), apply.getMember().getId())
                /*|| !Objects.equals(member.getId(), apply.getTeam().getLeader().getId())
                 */
                ) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Response
        return ApplyRes.of(apply);
    }

    public ApplyRes selectApply(Member member, Long applyId, StatusReq req){
        //Validation: member나 Apply가 유효하지 않거나, 리더가 아닌 경우 예외 처리
        if(member == null){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        Apply apply = applyRepository.findApplyWithTeam(applyId);
        if(apply == null){
            throw new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION);
        }

        if(apply.getTeam().getLeader().getId() != member.getId()){
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
        //Validation: member나 Apply가 유효하지 않거나, 리더가 아닌 경우 예외 처리
        if(member == null){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        Apply apply = applyRepository.findApplyWithTeam(applyId);
        if(apply == null){
            throw new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION);
        }

        if(apply.getTeam().getLeader().getId() != member.getId()){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Business Logic
        apply.setViewed(true);

        applyRepository.save(apply);

        //Response
        return ApplyRes.of(apply);
    }

    public void cancelApply(Member member, Long applyId) {
        //Validation: member나 Apply가 유효하지 않거나, 지원자 아닌 경우 예외 처리
        if(member == null){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        Apply apply = applyRepository.findById(applyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_APPLY_EXCEPTION));

        if(!Objects.equals(apply.getMember().getId(), member.getId())){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        //Business Logic
        applyRepository.delete(apply);
    }
}
