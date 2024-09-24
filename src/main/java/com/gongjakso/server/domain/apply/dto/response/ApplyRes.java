package com.gongjakso.server.domain.apply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gongjakso.server.domain.apply.entity.Apply;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record ApplyRes(

        @NotNull
        Long id,

        @NotNull
        Long teamId,

        @NotNull
        Long applicantId,

        @NotNull
        String applicantName,

        String applicantPhone,

        String applicantMajor,

        @Nullable
        Long portfolioId,

        @Nullable
        String portfolioName,

        Boolean isPrivate,

        @Size(max = 1000)
        String body,

        @Size(max = 20)
        @NotNull
        String status,

        @Size(max = 20)
        @NotNull
        String applyPart,

        int scrapCount,

        int dDay,

        LocalDate startedAt,

        LocalDate finishedAt,

        String title,

        String leaderName,

        Boolean isViewed,

        LocalDateTime deleteAt

) {
    public static ApplyRes of(Apply apply) {
        return ApplyRes.builder()
                .id(apply.getId())
                .teamId(apply.getTeam().getId())
                .title(apply.getTeam().getTitle())
                .applicantId(apply.getMember().getId())
                .applicantName(apply.getMember().getName())
                .applicantPhone(apply.getMember().getPhone())
                .applicantMajor(apply.getMember().getMajor())
                .leaderName(apply.getTeam().getMember().getName())
                .portfolioId(apply.getPortfolioInfo().getPortfolio() != null ? apply.getPortfolioInfo().getPortfolio().getId() : null)
                .portfolioName(apply.getPortfolioInfo().getPortfolio() != null ? apply.getPortfolioInfo().getPortfolio().getPortfolioName() : null)
                .isPrivate(apply.getPortfolioInfo().isPrivate())
                .body(apply.getBody())
                .status(apply.getStatus().getDescription())
                .applyPart(apply.getPart())
                .isViewed(apply.isViewed())
                .deleteAt(apply.getDeletedAt())
                .scrapCount(apply.getTeam().getScrapCount())
                .startedAt(apply.getTeam().getStartedAt())
                .finishedAt(apply.getTeam().getFinishedAt())
                .dDay(Period.between(LocalDate.now(), apply.getTeam().getFinishedAt()).getDays())
                .build();
    }
}
