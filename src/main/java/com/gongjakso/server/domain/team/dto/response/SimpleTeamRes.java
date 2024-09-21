package com.gongjakso.server.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.team.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimpleTeamRes(
    @Schema(description = "공모전 ID", example = "1")
    Long contestId,

    @Schema(description = "팀 ID", example = "1")
    Long id,

    @Schema(description = "팀 제목", example = "광화문광장 숏폼 공모전 참여자 모집")
    String title,

    @Schema(description = "팀장 ID", example = "1")
    Long leaderId,

    @Schema(description = "팀장 이름", example = "홍길동")
    String leaderName,

    @Schema(description = "모집 마감일", example = "2024-12-31")
    LocalDate recruitFinishedAt,

    @Schema(description = "활동 시작일", example = "2025-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate startedAt,

    @Schema(description = "활동 종료일", example = "2025-01-31")
    LocalDate finishedAt,

    @Schema(description = "D-day", example = "10")
    int dDay,

    @Schema(description = "스크랩 수", example = "10")
    int scrapCount,

    @Schema(description = "조회 수", example = "10")
    int viewCount
) {

    public static SimpleTeamRes of(Team team) {
        int dDay = 0;
        if (team.getRecruitFinishedAt() != null) {
            dDay = (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), team.getRecruitFinishedAt());
        }

        return SimpleTeamRes.builder()
            .contestId(team.getContest().getId())
            .id(team.getId())
            .title(team.getTitle())
            .leaderId(team.getMember().getId())
            .leaderName(team.getMember().getName())
            .recruitFinishedAt(team.getRecruitFinishedAt())
            .startedAt(team.getStartedAt())
            .finishedAt(team.getFinishedAt())
            .dDay(dDay)
            .scrapCount(team.getScrapCount())
            .viewCount(team.getViewCount())
            .build();
    }
}
