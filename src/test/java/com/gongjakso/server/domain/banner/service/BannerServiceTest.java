package com.gongjakso.server.domain.banner.service;

import com.gongjakso.server.domain.banner.dto.request.BannerReq;
import com.gongjakso.server.domain.banner.dto.response.BannerRes;
import com.gongjakso.server.domain.banner.entity.Banner;
import com.gongjakso.server.domain.banner.repository.BannerRepository;
import com.gongjakso.server.domain.banner.util.BannerUtilTest;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.util.s3.S3Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BannerServiceTest {

    @InjectMocks
    private BannerService bannerService;

    @Mock
    private BannerRepository bannerRepository;
    @Mock
    private S3Client s3Client;

    private Banner banner;
    private Member member;

    @BeforeEach
    void beforeEach() {
        banner = BannerUtilTest.buildContestBanner();
    }

    @Test
    @DisplayName("일반 사용자 배너 등록 테스트")
    void registerBannerByGeneralUserTest() {
        // given
        member = MemberUtilTest.buildMember();
        MultipartFile mockMultipartFile = new MockMultipartFile(
                "Example.jpg",
                "Example.jpg",
                "text/plain",
                "Mock File".getBytes(StandardCharsets.UTF_8)
        );
        BannerReq bannerReq = BannerUtilTest.buildContestBannerReq();

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> bannerService.registerBanner(member, bannerReq, mockMultipartFile));

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION);
    }

    @Test
    @DisplayName("관리자 배너 등록 테스트")
    void registerBannerByAdminTest() {
        // given
        member = MemberUtilTest.buildAdmin();
        MultipartFile mockMultipartFile = new MockMultipartFile(
                "Example.jpg",
                "Example.jpg",
                "text/plain",
                "Mock File".getBytes(StandardCharsets.UTF_8)
        );
        BannerReq bannerReq = BannerUtilTest.buildContestBannerReq();
        given(s3Client.upload(mockMultipartFile, "banner")).willReturn("https://aws.com/banner/example.jpg");
        System.out.println(banner);
        given(bannerRepository.save(banner)).willReturn(banner);

        // when
        BannerRes bannerRes = bannerService.registerBanner(member, bannerReq, mockMultipartFile);

        // then
        assertThat(bannerRes).isNotNull();
        assertThat(bannerRes.domainType()).isEqualTo(bannerReq.domainType());
    }
}
