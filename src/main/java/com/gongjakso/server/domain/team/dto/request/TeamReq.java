package com.gongjakso.server.domain.team.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.enumerate.MeetingMethod;
import com.gongjakso.server.domain.team.vo.RecruitPart;
import com.gongjakso.server.global.common.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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

    @Nullable
    @Schema(description = "시/도", example = "서울특별시")
    String province,

    @Nullable
    @Schema(description = "시/군/구", example = "강남구")
    String district,

    @Schema(description = "지원 파트")
    List<RecruitPartReq> recruitPart,

    @Schema(description = "모집 마감일", example = "2024-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate recruitFinishedAt,

    @Schema(description = "활동 시작일", example = "2025-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startedAt,

    @Schema(description = "활동 종료일", example = "2025-01-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate finishedAt,

    @Schema(description = "컨택 방법", example = "오픈카톡 = true | 구글폼 = false")
    boolean channelMethod,

    @Schema(description = "컨택 링크", example = "https://open.kakao.com/o/gongjakso")
    String channelLink
) {

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record RecruitPartReq(
            @NotNull
            @Schema(description = "파트 이름", example = "기획")
            String position,

            @NotNull
            @Schema(description = "파트 인원", example = "1")
            Integer recruitCount
        ) {

            public RecruitPart from() {
                Position position = Position.convert(this.position);
                return new RecruitPart(position, recruitCount, 0);
            }
        }

    public Team from(Member member, Contest contest) {
        MeetingMethod method = MeetingMethod.valueOf(meetingMethod);

        List<RecruitPart> recruitPartList = recruitPart.stream()
                .map(RecruitPartReq::from)
                .toList();

        return new Team(
                member,
                contest,
                title,
                body,
                totalCount,
                method,
                province,
                district,
                recruitPartList,
                recruitFinishedAt,
                startedAt,
                finishedAt,
                channelMethod,
                channelLink);
    }
}
