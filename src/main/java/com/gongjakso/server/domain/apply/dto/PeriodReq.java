package com.gongjakso.server.domain.apply.dto;

import java.time.LocalDate;

public record PeriodReq(
        LocalDate finishDate
) {
}