package com.gongjakso.server.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.team.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TeamRes(
    @Schema(description = "팀 ID", example = "1")
    Long id,

    @Schema(description = "멤버 ID", example = "1")
    Long memberId,

    @Schema(description = "멤버 이름", example = "홍길동")
    String memberName,

    @Schema(description = "공모전 ID", example = "1")
    Long contestId,

    @Schema(description = "공모전 제목", example = "2024 광화문광장 사진·짧은 영상(숏폼) 공모전")
    String contestTitle,

    @Schema(description = "팀 제목", example = "광화문광장 숏폼 공모전 참여자 모집")
    String title,

    @Schema(description = "팀 내용", example = "광화문광장 숏폼 공모전 참여자 모집합니다.")
    String body,

    @Schema(description = "총 인원", example = "5")
    int totalCount,

    @Schema(description = "합격 인원", example = "3")
    int passCount,

    @Schema(description = "모임 방식", example = "온라인 | 오프라인 | 하이브리드")
    String meetingMethod,

    @Schema(description = "시/도", example = "서울특별시")
    String province,

    @Schema(description = "시/군/구", example = "강남구")
    String district,

    @Schema(description = "모집 마감일", example = "2024-12-31")
    LocalDate recruitFinishedAt,

    @Schema(description = "활동 시작일", example = "2025-01-01")
    LocalDate startedAt,

    @Schema(description = "활동 종료일", example = "2025-01-31")
    LocalDate finishedAt,

    @Schema(description = "컨택 링크", example = "https://open.kakao.com/o/gongjakso")
    String channelLink,

    @Schema(description = "스크랩 수", example = "10")
    int scrapCount
) {
    public static TeamRes of(Team team) {
        return TeamRes.builder()
                .id(team.getId())
                .memberId(team.getMember().getId())
                .memberName(team.getMember().getName())
                .contestId(team.getContest().getId())
                .contestTitle(team.getContest().getTitle())
                .title(team.getTitle())
                .body(team.getTitle())
                .totalCount(team.getTotalCount())
                .passCount(team.getPassCount())
                .meetingMethod(team.getMeetingMethod().getDescription())
                .province(team.getProvince())
                .district(team.getDistrict())
                .recruitFinishedAt(team.getRecruitFinishedAt())
                .startedAt(team.getStartedAt())
                .finishedAt(team.getFinishedAt())
                .channelLink(team.getChannelLink())
                .scrapCount(team.getScrapCount())
                .build();
    }
}
