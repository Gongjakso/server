package com.gongjakso.server.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record MemberReq(@NotNull String name,
                        @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$") String phone,
                        String status,
                        String major,
                        String job) {
}
