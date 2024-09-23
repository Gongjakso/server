package com.gongjakso.server.domain.apply.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplyStatus {
    COMPLETED("합류 대기중"),
    ACCEPTED("합류 완료"),
    REJECTED("미선발");

    private final String description;
}
