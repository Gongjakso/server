package com.gongjakso.server.domain.contest.service;

import com.gongjakso.server.domain.contest.dto.response.ContestRes;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.domain.contest.util.ContestUtilTest;
import com.gongjakso.server.global.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindContestServiceTest {
    @Mock
    private ContestRepository contestRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private ContestService contestService;

    @BeforeEach
    void setup(){

    }

    @Test
    @DisplayName("공모전 검색 테스트")
    void findContestTest(){
        Contest contest = ContestUtilTest.buildContest();
        given(contestRepository.findById(1L)).willReturn(Optional.of(contest));

        ContestRes contestRes = contestService.find(1L,request,response);

        verify(contestRepository).findById(1L);
        assertNotNull(contestRes);
    }

    @Test
    @DisplayName("공모전 검색 시 존재하지 않는 공모전 테스트")
    void findNotExistContestTest(){
        given(contestRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> contestService.find(1L,request,response))
                .isInstanceOf(ApplicationException.class);
    }
}
