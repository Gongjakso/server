package com.gongjakso.server.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MemberReq(@NotNull String name,
                        String status,
                        String major,
                        String job) {
}
