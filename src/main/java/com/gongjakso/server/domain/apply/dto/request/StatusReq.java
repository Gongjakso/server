package com.gongjakso.server.domain.apply.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.apply.enumerate.ApplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record StatusReq(

        @Size(max = 20)
        @NotNull
        @Schema(description = "지원 상태", example = "SELECTED")
        ApplyStatus status
) {
}
