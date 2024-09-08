package com.gongjakso.server.domain.team.enumerate;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum MeetingMethod {
    OFFLINE("오프라인"),
    ONLINE("온라인"),
    HYBRID("하이브리드");

    private final String description;

    MeetingMethod(String description) {
        this.description = description;
    }
}
