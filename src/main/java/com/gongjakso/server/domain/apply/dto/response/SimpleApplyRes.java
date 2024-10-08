package com.gongjakso.server.domain.apply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gongjakso.server.domain.apply.entity.Apply;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SimpleApplyRes(
        @NotNull
        Long id,

        @NotNull
        Long teamId,

        @NotNull
        Long applicantId,
        String applicantName,
        Long portfolioId,
        String portfolioName,
        String dataType,
        String status,
        String applyPart,
        Boolean isViewed
) {
    public static SimpleApplyRes of(Apply apply){
        return SimpleApplyRes.builder()
                .id(apply.getId())
                .teamId(apply.getTeam().getId())
                .applicantId(apply.getMember().getId())
                .applicantName(apply.getMember().getName())
                .portfolioId(apply.getPortfolioInfo().getPortfolio() != null ? apply.getPortfolioInfo().getPortfolio().getId() : null)
                .portfolioName(apply.getPortfolioInfo().getPortfolio() != null ? apply.getPortfolioInfo().getPortfolio().getPortfolioName() : null)
                .dataType(apply.getPortfolioInfo().getDataType())
                .status(apply.getStatus().name())
                .applyPart(apply.getPart())
                .isViewed(apply.isViewed())
                .build();
    }
}
