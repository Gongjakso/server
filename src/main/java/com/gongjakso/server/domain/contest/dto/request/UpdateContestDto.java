package com.gongjakso.server.domain.contest.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateContestDto(
        @Nullable
        @Size(min=1,max=150)
        String title,
        @Nullable
        String body,
        @Nullable
        String contestLink,
        @Nullable
        @Size(min=1,max=100)
        String institution,
        @Nullable
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startedAt,
        @Nullable
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate finishedAt
) {
}
