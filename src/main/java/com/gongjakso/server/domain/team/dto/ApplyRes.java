package com.gongjakso.server.domain.team.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.team.entity.Apply;
import com.gongjakso.server.domain.team.enumerate.ApplyStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

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

        Long scrapCount,

        Long remainingDays,

        LocalDateTime startedAt,

        LocalDateTime finishedAt,

        String teamName,

        String leaderName

) {
    public static ApplyRes of(Apply apply) {
        return ApplyRes.builder()
                .applyId(apply.getId())
                .teamId(apply.getTeam().getId())
                .memberId(apply.getMember().getId())
                .portfolioId(apply.getPortfolio() != null ? apply.getPortfolio().getId() : null)
                .body(apply.getBody())
                .status(apply.getStatus())
                .part(apply.getPart())
                .build();
    }


    // 스크랩 수, 남은 날짜, 팀 시작일, 팀 종료일, 팀 이름, 팀 리더 이름 추가 필요
    public static Page<ApplyRes> toPagination(Page<Apply> applyPage) {
        return applyPage.map(apply -> ApplyRes.builder()
                .applyId(apply.getId())
                .teamId(apply.getTeam().getId())
                .memberId(apply.getMember().getId())
                .portfolioId(apply.getPortfolio() != null ? apply.getPortfolio().getId() : null)
                .body(apply.getBody())
                .status(apply.getStatus())
                .part(apply.getPart())
                .build());
    }
}
