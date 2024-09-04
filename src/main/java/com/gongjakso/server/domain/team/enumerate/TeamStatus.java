package com.gongjakso.server.domain.team.enumerate;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum TeamStatus {

    ACTIVE("모집 중"),
    EXTENSION("모집 연장"),
    CANCELED("모집 취소"),
    CLOSED("모집 마감"),;

    private final String description;

    TeamStatus(String description) {
        this.description = description;
    }
}
