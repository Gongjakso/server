package com.gongjakso.server.domain.team.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.enumerate.MeetingMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TeamReq(
    @NotEmpty
    @Size(min = 1, max = 50)
    String title,

    @Size(max = 1000)
    String body,

    @NotEmpty
    int totalCount,

    @NotEmpty
    String meetingMethod,

    String province,

    String district,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate recruitFinishedAt,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startedAt,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate finishedAt,

    String channelLink
) {
    public Team from() {
        MeetingMethod method = MeetingMethod.valueOf(meetingMethod);

        return Team.builder()
                .title(title)
                .body(body)
                .totalCount(totalCount)
                .meetingMethod(method)
                .province(province)
                .district(district)
                .recruitFinishedAt(recruitFinishedAt)
                .startedAt(startedAt)
                .finishedAt(finishedAt)
                .channelLink(channelLink)
                .build();
    }
}
