package com.gongjakso.server.domain.banner.service;

import com.gongjakso.server.domain.banner.dto.response.BannerRes;
import com.gongjakso.server.domain.banner.entity.Banner;
import com.gongjakso.server.domain.banner.enumerate.DomainType;
import com.gongjakso.server.domain.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public List<BannerRes> getMainImageList() {
        // Business Logic
        List<Banner> bannerList = bannerRepository.findAllByDomainTypeAndDeletedAtIsNullOrderByPriorityAsc(DomainType.MAIN);

        // Response
        return bannerList.stream()
                .map(BannerRes::of)
                .collect(Collectors.toList());
    }

    public List<BannerRes> getProjectImageList() {
        // Business Logic
        List<Banner> bannerList = bannerRepository.findAllByDomainTypeAndDeletedAtIsNullOrderByPriorityAsc(DomainType.PROJECT);

        // Response
        return bannerList.stream()
                .map(BannerRes::of)
                .collect(Collectors.toList());
    }

    public List<BannerRes> getContestImageList() {
        // Business Logic
        List<Banner> bannerList = bannerRepository.findAllByDomainTypeAndDeletedAtIsNullOrderByPriorityAsc(DomainType.CONTEST);

        // Response
        return bannerList.stream()
                .map(BannerRes::of)
                .collect(Collectors.toList());
    }
}
