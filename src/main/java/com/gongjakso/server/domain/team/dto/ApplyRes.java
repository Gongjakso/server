package com.gongjakso.server.domain.team.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.team.entity.Apply;
import com.gongjakso.server.domain.team.enumerate.ApplyStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApplyRes(

        @NotNull
        Long applyId,

        @NotNull
        Long teamId,

        @NotNull
        Long memberId,

        @Nullable
        Long portfolioId,

        @Size(max = 500)
        String body,

        @Size(max = 20)
        @NotNull
        ApplyStatus status,

        @Size(max = 20)
        @NotNull
        String part,

        int scrapCount,

        int remainingDays,

        LocalDate startedAt,

        LocalDate finishedAt,

        String teamName,

        String leaderName,

        Boolean isViewed,

        LocalDateTime deleteAt

) {
    public static ApplyRes of(Apply apply) {
        return ApplyRes.builder()
                .applyId(apply.getId())
                .teamId(apply.getTeam().getId())
                .teamName(apply.getTeam().getTitle())
                .memberId(apply.getMember().getId())
                .leaderName(apply.getTeam().getMember().getName())
                .portfolioId(apply.getPortfolio() != null ? apply.getPortfolio().getId() : null)
                .body(apply.getBody())
                .status(apply.getStatus())
                .part(apply.getPart())
                .isViewed(apply.isViewed())
                .deleteAt(apply.getDeletedAt())
                .scrapCount(apply.getTeam().getScrapCount())
                .startedAt(apply.getTeam().getStartedAt())
                .finishedAt(apply.getTeam().getFinishedAt())
                .remainingDays(Period.between(LocalDate.now(), apply.getTeam().getFinishedAt()).getDays())
                .build();
    }
}
