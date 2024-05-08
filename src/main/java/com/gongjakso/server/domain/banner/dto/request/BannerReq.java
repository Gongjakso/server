package com.gongjakso.server.domain.banner.dto.request;

import com.gongjakso.server.domain.banner.entity.Banner;
import com.gongjakso.server.domain.banner.enumerate.DomainType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BannerReq(
    @Schema(
            description = "배너 게시 위치를 (MAIN | PROJECT | CONTEST)의 ENUM으로 관리",
            defaultValue = "MAIN"
    )
    @NotBlank DomainType domainType,
    @Schema(
        description = "배너 클릭 시, 리다이렉트될 주소",
        defaultValue = "https://instagram.com/official_gongjakso"
    )
    String linkUrl,
    @Schema(
            description = "배너 게시 순서를 의미하며, 숫자가 낮을수록 높은 우선순위를 가짐",
            defaultValue = "1"
    )
    @NotEmpty Integer priority,
    @Schema(
            description = "배너 삭제가 아닌 데이터로 가지고 있지만, 배너로 임시로 게시하지 않을 때 사용",
            defaultValue = "true"
    )
    @NotNull Boolean isPost
) {

    public Banner from(String imageUrl) {
        return Banner.builder()
                .domainType(this.domainType)
                .imageUrl(imageUrl)
                .linkUrl(this.linkUrl)
                .priority(this.priority)
                .isPost(this.isPost)
                .build();
    }
}
