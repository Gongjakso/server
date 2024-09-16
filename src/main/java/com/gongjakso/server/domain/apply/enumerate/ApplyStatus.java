package com.gongjakso.server.domain.apply.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplyStatus {
    COMPLETED("지원 완료"),
    ACCEPTED("합격"),
    REJECTED("불합격");

    private final String description;
}
