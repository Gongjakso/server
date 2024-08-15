package com.gongjakso.server.domain.team.enumerate;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum TeamStatus {

    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String description;

    TeamStatus(String description) {
        this.description = description;
    }
}
