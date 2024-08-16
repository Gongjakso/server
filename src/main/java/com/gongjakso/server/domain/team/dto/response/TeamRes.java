package com.gongjakso.server.domain.team.dto.response;

import com.gongjakso.server.domain.team.entity.Team;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TeamRes(
    Long id,
    String memberId,
    String memberName,
    String contestId,
    String contestName,
    String title,
    String body,
    int totalCount,
    int passCount,
    String meetingMethod,
    String province,
    String district,
    LocalDate recruitFinishedAt,
    LocalDate startedAt,
    LocalDate finishedAt,
    String channelLink,
    int scrapCount
) {
    public static TeamRes of(Team team, String memberId, String memberName, String contestId, String contestName) {
        return TeamRes.builder()
                .id(team.getId())
                .memberId(memberId)
                .memberName(memberName)
                .contestId(contestId)
                .contestName(contestName)
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
