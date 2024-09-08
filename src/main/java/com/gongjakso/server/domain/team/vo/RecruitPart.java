package com.gongjakso.server.domain.team.vo;

import com.gongjakso.server.global.common.Position;
import lombok.Builder;

@Builder
public record RecruitPart(
        Position position,
        Integer recruitCount,
        Integer passCount
) {
}
