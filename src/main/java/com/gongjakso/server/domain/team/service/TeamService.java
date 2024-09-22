package com.gongjakso.server.domain.team.service;

import com.gongjakso.server.domain.apply.dto.response.SimpleApplyRes;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyStatus;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.dto.request.TeamReq;
import com.gongjakso.server.domain.team.dto.response.ScrapRes;
import com.gongjakso.server.domain.team.dto.response.SimpleTeamRes;
import com.gongjakso.server.domain.team.dto.response.TeamRes;
import com.gongjakso.server.domain.team.entity.Scrap;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.enumerate.TeamStatus;
import com.gongjakso.server.domain.team.repository.ScrapRepository;
import com.gongjakso.server.domain.team.repository.TeamRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;
    private final ScrapRepository scrapRepository;
    private final ApplyRepository applyRepository;

    @Transactional
    public TeamRes createTeam(Member member, Long contestId, TeamReq teamReq) {
        // Validation
        Contest contest = contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));

        // Business Logic
        Team team = teamReq.from(member, contest);
        Team savedTeam = teamRepository.save(team);

        // Response
        return TeamRes.of(savedTeam, "LEADER");
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
        return TeamRes.of(updatedTeam, "LEADER");
    }

    @Transactional
    public TeamRes extendRecruit(Member member, Long contestId, Long teamId, LocalDate extendDate) {
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
        team.updateTeamStatus(TeamStatus.EXTENSION);
        team.extendRecruitFinishedAt(extendDate);
        Team updatedTeam = teamRepository.save(team);

        // Response
        return TeamRes.of(updatedTeam, "LEADER");
    }

    @Transactional
    public TeamRes closeRecruit(Member member, Long contestId, Long teamId) {
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
        team.updateTeamStatus(TeamStatus.CLOSED);
        Team updatedTeam = teamRepository.save(team);

        // Response
        return TeamRes.of(updatedTeam, "LEADER");
    }

    @Transactional
    public TeamRes cancelRecruit(Member member, Long contestId, Long teamId) {
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
        team.updateTeamStatus(TeamStatus.CANCELED);
        Team updatedTeam = teamRepository.save(team);

        // Response
        return TeamRes.of(updatedTeam, "LEADER");
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

    @Transactional
    public TeamRes getTeam(Member member, Long contestId, Long teamId, HttpServletRequest request, HttpServletResponse response) {
        // Validation
        contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
        if(!team.getContest().getId().equals(contestId)) {
            throw new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION);
        }

        getPassCount(team);

        if(member != null && team.getMember().getId().equals(member.getId())){
            return TeamRes.of(team, "LEADER");
        }else if(member != null &&  applyRepository.findByTeamIdAndMemberIdAndDeletedAtIsNull(teamId, member.getId()).isPresent()){
            Apply apply = applyRepository.findByTeamIdAndMemberIdAndDeletedAtIsNull(teamId, member.getId())
                    .orElseThrow(() -> new ApplicationException(ErrorCode.APPLY_NOT_FOUND_EXCEPTION));
            return TeamRes.of(team, "APPLIER", apply);
        }

        updateView(team, request, response);

        // Business Logic
        return TeamRes.of(team, "GENERAL");
    }

    public Page<SimpleTeamRes> getTeamListWithContest(Long contestId, String province, String district, Pageable pageable) {
        // Validation
        contestRepository.findByIdAndDeletedAtIsNull(contestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTEST_NOT_FOUND_EXCEPTION));

        // Business Logic & Response
        return teamRepository.findPaginationWithContest(contestId, province, district, pageable);
    }

    // TODO: Full-text Search 개선 진행 예정
    public Page<SimpleTeamRes> getTeamListWithoutContest(String province, String district, String keyword, Pageable pageable) {
        // Business Logic & Response
        return teamRepository.findPaginationWithoutContest(province, district, keyword, pageable);
    }

    public Page<SimpleTeamRes> getMyRecruitTeamList(Member member, Pageable pageable) {
        // Business Logic & Response
        return teamRepository.findRecruitPagination(member.getId(), pageable);
    }

    public Page<SimpleTeamRes> getMyApplyTeamList(Member member, Pageable pageable) {
        // Business Logic & Response
        return teamRepository.findApplyPagination(member.getId(), pageable);
    }

    public Page<SimpleTeamRes> getMyParticipateTeamList(Member member, Pageable pageable) {
        // Business Logic & Response
        return teamRepository.findParticipatePagination(member.getId(), pageable);
    }

    @Transactional
    public ScrapRes scrapTeam(Member member, Long teamId) {
        // Validation
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));


        // Business Logic
        Scrap scrap = scrapRepository.findScrapByMemberIdAndTeamId(member.getId(), team.getId()).orElse(null);

        if(scrap == null) {
            scrapRepository.save(Scrap.builder()
                    .member(member)
                    .team(team)
                    .build());
        }

        team.updateScrapCount(team.getScrapCount() + 1);
        Team updateTeam = teamRepository.save(team);

        // Response
        return ScrapRes.builder()
                .scrapCount(updateTeam.getScrapCount())
                .build();
    }

    @Transactional
    public ScrapRes cancelScrapTeam(Member member, Long teamId) {
        // Validation
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));

        // Business Logic
        scrapRepository.deleteScrapByMemberIdAndTeamId(member.getId(), team.getId());

        team.updateScrapCount(team.getScrapCount() - 1);
        Team updateTeam = teamRepository.save(team);

        // Response
        return ScrapRes.builder()
                .scrapCount(updateTeam.getScrapCount())
                .build();
    }

    public Page<SimpleTeamRes> getScrapTeamList(Member member, Pageable pageable) {
        // Business Logic & Response
        return teamRepository.findScrapPagination(member.getId(), pageable);
    }

    public ScrapRes checkScrapTeam(Member member, Long teamId) {
        // Validation
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));

        // Business Logic
        Scrap scrap = scrapRepository.findScrapByMemberIdAndTeamId(member.getId(), team.getId())
                .orElse(null);

        // Response
        return (scrap != null)
                ? ScrapRes.builder()
                    .isScrap(true).build()
                : ScrapRes.builder()
                    .isScrap(false).build();
    }

    @Transactional
    public TeamRes changeTeamStatus(Member member, Long contestId, Long teamId, String status) {
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
        TeamStatus teamStatus = TeamStatus.checkActiveORFinished(status);

        // Business Logic
        team.updateTeamStatus(teamStatus);
        Team updatedTeam = teamRepository.save(team);

        // Response
        return TeamRes.of(updatedTeam, "LEADER", null);
    }

    public List<SimpleApplyRes> getApplyList(Member member, Long teamId) {
        // Validation
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
        if (!team.getMember().getId().equals(member.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        // Business Logic & Response
        return applyRepository.findAllByTeamIdAndDeletedAtIsNull(teamId).stream()
                .map(SimpleApplyRes::of)
                .toList();
    }

    public void updateView(Team team, HttpServletRequest request, HttpServletResponse response) {
        boolean hasViewed = false;
        Cookie[] cookies = request.getCookies();

        String COOKIE_NAME = "team_view";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    hasViewed = true;
                    break;
                }
            }
        }

        if (!hasViewed) {
            team.updateViewCount(team);
            // 세션 쿠키 설정
            Cookie newCookie = new Cookie(COOKIE_NAME, "viewed");
            newCookie.setMaxAge(-1); // 브라우저 세션이 끝날 때까지 유효
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
    }

    public void getPassCount(Team team) {
        int passCount = applyRepository.countByTeamIdAndStatusAndDeletedAtIsNull(team.getId(), ApplyStatus.ACCEPTED);
        team.updatePassCount(passCount);
    }
}
