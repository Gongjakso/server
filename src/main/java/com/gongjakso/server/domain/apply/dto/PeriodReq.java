package com.gongjakso.server.domain.apply.dto;

import java.time.LocalDateTime;

public record PeriodReq(
        LocalDateTime endDate
) {
}