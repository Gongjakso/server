package com.gongjakso.server.domain.team.service;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.domain.contest.util.ContestUtilTest;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.domain.team.dto.request.TeamReq;
import com.gongjakso.server.domain.team.dto.response.TeamRes;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.repository.ScrapRepository;
import com.gongjakso.server.domain.team.repository.TeamRepository;
import com.gongjakso.server.domain.team.util.TeamUtilTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private ScrapRepository scrapRepository;

    @BeforeEach
    void setUp() {
        teamService = new TeamService(teamRepository, contestRepository, scrapRepository);
    }

    @Test
    @DisplayName("")
    void createTeam() {
        // given
        Member member = MemberUtilTest.buildMember();

        Contest contest = ContestUtilTest.buildContest();

        TeamReq teamReq = null;
        Team team = TeamUtilTest.buildteam();

        when(contestRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(java.util.Optional.of(contest));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // when
        TeamRes result = teamService.createTeam(member, 1L, teamReq);

        // then
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(contestRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    @DisplayName("")
    void deleteTeam() {
        // given

        // when

        // then
    }
}