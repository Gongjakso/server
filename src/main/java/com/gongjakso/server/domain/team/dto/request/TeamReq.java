package com.gongjakso.server.domain.team.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.enumerate.MeetingMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TeamReq(

    @Schema(description = "팀 제목", example = "광화문광장 숏폼 공모전 참여자 모집")
    @NotEmpty
    @Size(min = 1, max = 50)
    String title,

    @Schema(description = "팀 내용", example = "광화문광장 숏폼 공모전 참여자 모집합니다.")
    @Size(max = 1000)
    String body,

    @Schema(description = "총 인원", example = "5")
    @NotEmpty
    int totalCount,

    @Schema(description = "모임 방식", example = "온라인 | 오프라인 | 하이브리드")
    @NotEmpty
    String meetingMethod,

    @Schema(description = "시/도", example = "서울특별시")
    String province,

    @Schema(description = "시/군/구", example = "강남구")
    String district,

    @Schema(description = "모집 마감일", example = "2024-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate recruitFinishedAt,

    @Schema(description = "활동 시작일", example = "2025-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startedAt,

    @Schema(description = "활동 종료일", example = "2025-01-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate finishedAt,

    @Schema(description = "컨택 링크", example = "https://open.kakao.com/o/gongjakso")
    String channelLink
) {
    public Team from(Member member, Contest contest) {
        MeetingMethod method = MeetingMethod.valueOf(meetingMethod);

        return new Team(
                member,
                contest,
                title,
                body,
                totalCount,
                method,
                province,
                district,
                recruitFinishedAt,
                startedAt,
                finishedAt,
                channelLink);
    }
}
