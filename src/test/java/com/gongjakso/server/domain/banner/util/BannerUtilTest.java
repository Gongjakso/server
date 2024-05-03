package com.gongjakso.server.domain.banner.util;

import com.gongjakso.server.domain.banner.dto.request.BannerReq;
import com.gongjakso.server.domain.banner.entity.Banner;
import com.gongjakso.server.domain.banner.enumerate.DomainType;

public class BannerUtilTest {

    public static BannerReq buildContestBannerReq() {
        return BannerReq.builder()
                .domainType(DomainType.CONTEST)
                .linkUrl("https://main--gongjakso.netlify.app/")
                .priority(1)
                .build();

    }

    public static Banner buildContestBanner() {
        return Banner.builder()
                .domainType(DomainType.CONTEST)
                .imageUrl("https://aws.com/banner/example.jpg")
                .linkUrl("https://main--gongjakso.netlify.app/")
                .priority(1)
                .isPost(Boolean.TRUE)
                .build();
    }
}
