package com.gongjakso.server.domain.banner.service;

import com.gongjakso.server.domain.banner.dto.request.BannerReq;
import com.gongjakso.server.domain.banner.dto.response.BannerRes;
import com.gongjakso.server.domain.banner.entity.Banner;
import com.gongjakso.server.domain.banner.enumerate.DomainType;
import com.gongjakso.server.domain.banner.repository.BannerRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.util.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final S3Client s3Client;

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

    @Transactional
    public BannerRes registerBanner(Member member, BannerReq bannerReq, MultipartFile multipartFile) {
        // Validation
        if(!member.getMemberType().equals(MemberType.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        if(multipartFile == null || multipartFile.isEmpty()) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }

        // Business Logic
        String imageUrl = s3Client.upload(multipartFile, "banner");
        Banner banner = bannerReq.from(imageUrl);
        Banner savedBanner = bannerRepository.save(banner);

        // Response
        return BannerRes.of(savedBanner);
    }

    @Transactional
    public BannerRes updateBanner(Member member, Long bannerId, BannerReq bannerReq, MultipartFile multipartFile) {
        // Validation
        if(!member.getMemberType().equals(MemberType.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        String imageUrl = "";
        if(multipartFile != null && !multipartFile.isEmpty()) {
            s3Client.delete(banner.getImageUrl());
            imageUrl = s3Client.upload(multipartFile, "banner");
        }

        banner.update(bannerReq, imageUrl);
        Banner updatedBanner = bannerRepository.save(banner);

        // Response
        return BannerRes.of(updatedBanner);
    }

    @Transactional
    public BannerRes changeIsPost(Member member, Long bannerId) {
        // Validation
        if(!member.getMemberType().equals(MemberType.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        banner.changeIsPost();
        Banner updatedBanner = bannerRepository.save(banner);

        // Response
        return BannerRes.of(updatedBanner);
    }

    @Transactional
    public void deleteBanner(Member member, Long bannerId) {
        // Validation
        if(!member.getMemberType().equals(MemberType.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        s3Client.delete(banner.getImageUrl());
        bannerRepository.delete(banner);

        // Response
    }
}
