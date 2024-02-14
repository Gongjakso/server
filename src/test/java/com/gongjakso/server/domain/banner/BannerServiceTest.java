package com.gongjakso.server.domain.banner;

import com.gongjakso.server.domain.banner.dto.response.BannerRes;
import com.gongjakso.server.domain.banner.entity.Banner;
import com.gongjakso.server.domain.banner.enumerate.DomainType;
import com.gongjakso.server.domain.banner.repository.BannerRepository;
import com.gongjakso.server.domain.banner.service.BannerService;
import com.gongjakso.server.global.util.BannerUtilTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class BannerServiceTest {

    private BannerService bannerService;

    @Mock
    private BannerRepository bannerRepository;

    @BeforeEach
    void beforeEach() {
        bannerService = new BannerService(bannerRepository);
    }

    @Test
    @DisplayName("배너 리스트 반환 테스트 - 정상")
    void getBannerListSuccess() {
        // given
        List<Banner> bannerList = BannerUtilTest.createBannerList();
        given(bannerRepository.findAllByDomainTypeAndDeletedAtIsNullOrderByPriorityAsc(DomainType.MAIN)).willReturn(bannerList);

        // when
        List<BannerRes> mainBannerList = bannerService.getMainImageList();

        // then
        assertThat(mainBannerList).isNotNull();
        //assertThat()
    }

    @Test
    @DisplayName("배너 리스트 반환 테스트 - 실패")
    void getBannerListFail() {
        // given

        // when

        // then
    }
}
