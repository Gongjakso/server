package com.gongjakso.server.domain.contest.service;

import com.gongjakso.server.domain.contest.dto.request.UpdateContestDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdateContestServiceTest {
    @Mock
    ContestRepository contestRepository;
    @Mock
    S3Client s3Client;
    @InjectMocks
    ContestService contestService;

    @Test
    @DisplayName("memberType = general인 경우 공모전 업데이트 테스트")
    void updateGeneralMemberTypeTest(){
        Member generalMember = MemberUtilTest.buildMemberByType(MemberType.GENERAL);
        assertThatThrownBy(()-> contestService.update(generalMember,1L,null,null))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("memberType = admin인 경우 공모전 업데이트 테스트")
    void updateAdminMemberTypeTest(){
        Member adminMember = MemberUtilTest.buildMemberByType(MemberType.ADMIN);
        Contest contest = ContestUtilTest.buildContest();
        MultipartFile image = mock(MultipartFile.class);
        UpdateContestDto updateContestDto = ContestUtilTest.buildUpdateContestDto();
        given(contestRepository.findById(1L)).willReturn(Optional.of(contest));
        given(image.isEmpty()).willReturn(false);
        given(s3Client.upload(image,"contest")).willReturn("https://s3.amazonaws.com/gongjakso-bucket/contest/image");

        contestService.update(adminMember,1L,image,updateContestDto);

        verify(s3Client).upload(image,"contest");
        verify(contestRepository).save(any(Contest.class));

    }
}
