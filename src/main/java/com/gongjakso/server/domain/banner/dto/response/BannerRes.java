package com.gongjakso.server.domain.banner.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.banner.entity.Banner;
import com.gongjakso.server.domain.banner.enumerate.DomainType;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BannerRes(
    Long bannerId,
    DomainType domainType,
    String imageUrl,
    String linkUrl,
    Integer priority,
    Boolean isPost
) {

    public static BannerRes of(Banner banner) {
        return BannerRes.builder()
                .bannerId(banner.getId())
                .domainType(banner.getDomainType())
                .imageUrl(banner.getImageUrl())
                .linkUrl(banner.getLinkUrl())
                .priority(banner.getPriority())
                .isPost(banner.getIsPost())
                .build();
    }
}
