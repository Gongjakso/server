package com.gongjakso.server.domain.team.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.team.enumerate.ApplyStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record StatusReq(

        @Size(max = 20)
        @NotNull
        ApplyStatus status
) {
}
