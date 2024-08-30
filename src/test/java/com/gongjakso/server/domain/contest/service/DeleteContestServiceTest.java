package com.gongjakso.server.domain.contest.service;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.domain.contest.util.ContestUtilTest;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.util.s3.S3Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteContestServiceTest {
    @Mock
    ContestRepository contestRepository;
    @Mock
    S3Client s3Client;
    @InjectMocks
    ContestService contestService;

    @Test
    @DisplayName("memberType = general인 경우 공모전 삭제 테스트")
    void deleteGeneralMemberTypeTest(){
        Member generalmember = MemberUtilTest.buildMemberByType(MemberType.GENERAL);

        assertThatThrownBy(() -> contestService.delete(generalmember, 1L))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("memberType = admin인 경우 공모전 삭제")
    void deleteAdminMemberTypeTest(){
        Member adminMember = MemberUtilTest.buildMemberByType(MemberType.ADMIN);
        Contest contest = ContestUtilTest.buildContest();
        given(contestRepository.findById(1L)).willReturn(Optional.of(contest));

        contestService.delete(adminMember,1L);

        verify(s3Client).delete(anyString());
        verify(contestRepository).delete(any(Contest.class));
    }
}
