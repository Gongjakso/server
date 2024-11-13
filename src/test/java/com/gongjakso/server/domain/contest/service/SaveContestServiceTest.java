package com.gongjakso.server.domain.contest.service;

import com.gongjakso.server.domain.contest.dto.request.ContestReq;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.contest.repository.ContestRepository;
import com.gongjakso.server.domain.contest.util.ContestUtilTest;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.util.s3.S3Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveContestServiceTest {
    @Mock
    ContestRepository contestRepository;
    @Mock
    S3Client s3Client;
    @InjectMocks
    ContestService contestService;

    private ContestReq contestReq;
    private MultipartFile image;

    @BeforeEach
    void setup(){
        contestReq = ContestUtilTest.buildContestReq();
        image = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("memberType = general일 경우 오류 테스트")
    void checkGeneralMemberTypeTest(){
        Member generalMember = MemberUtilTest.buildMemberByType(MemberType.GENERAL);
        assertThatThrownBy(() -> contestService.save(generalMember, image, contestReq))
                .isInstanceOf(ApplicationException.class);
        verify(contestRepository,never()).save(any(Contest.class));
        verify(s3Client,never()).upload(any(MultipartFile.class),anyString());
    }

    @Test
    @DisplayName("memberType = admin일 경우 contest 저장 , 이미지 있음")
    void saveContestTest(){
        Member adminMember = MemberUtilTest.buildMemberByType(MemberType.ADMIN);
        given(image.isEmpty()).willReturn(false);
        given(s3Client.upload(image,"contest")).willReturn("https://s3.amazonaws.com/gongjakso-bucket/contest/image");

        contestService.save(adminMember,image,contestReq);

        verify(s3Client).upload(image,"contest");
        verify(contestRepository).save(any(Contest.class));
    }

    @Test
    @DisplayName("memberType = admin일 경우 contest 저장, 이미지 없음")
    void saveContestNoImageTest(){
        Member adminMember = MemberUtilTest.buildMemberByType(MemberType.ADMIN);
        given(image.isEmpty()).willReturn(true);

        contestService.save(adminMember,image,contestReq);

        verify(s3Client,never()).upload(any(MultipartFile.class),anyString());
        verify(contestRepository).save(any(Contest.class));
    }

}
