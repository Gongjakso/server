package com.gongjakso.server.domain.apply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.team.vo.RecruitPart;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record ApplyRes(

        @NotNull
        Long id,

        @NotNull
        Long teamId,

        @NotNull
        Long contestId,

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

        List<RecruitPartRes> recruitPart,

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

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record RecruitPartRes(
            @Schema(description = "지원 파트", example = "기획")
            String position,

            @Schema(description = "모집 인원", example = "3")
            Integer recruitCount,

            @Schema(description = "합격 인원", example = "2")
            Integer passCount
    ) {

        public static RecruitPartRes of(RecruitPart recruitPart) {
            return RecruitPartRes.builder()
                    .position(recruitPart.position().getKoreanName())
                    .recruitCount(recruitPart.recruitCount())
                    .passCount(recruitPart.passCount())
                    .build();
        }

    }

    public static ApplyRes of(Apply apply) {
        List<RecruitPartRes> recruitPartRes = (apply.getTeam().getRecruitPart() != null) ? apply.getTeam().getRecruitPart().stream()
                .map(RecruitPartRes::of)
                .toList() : null;

        return ApplyRes.builder()
                .id(apply.getId())
                .teamId(apply.getTeam().getId())
                .contestId(apply.getTeam().getContest().getId())
                .title(apply.getTeam().getTitle())
                .applicantId(apply.getMember().getId())
                .applicantName(apply.getMember().getName())
                .applicantPhone(apply.getMember().getPhone())
                .applicantMajor(apply.getMember().getMajor())
                .leaderName(apply.getTeam().getMember().getName())
                .portfolioId(apply.getPortfolioInfo().isPrivate() ? null : apply.getPortfolioInfo().getPortfolio().getId())
                .portfolioName(apply.getPortfolioInfo().isPrivate() ? null : apply.getPortfolioInfo().getPortfolio().getPortfolioName())
                .isPrivate(apply.getPortfolioInfo().isPrivate())
                .body(apply.getBody())
                .status(apply.getStatus().getDescription())
                .recruitPart(recruitPartRes)
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
