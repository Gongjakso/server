package com.gongjakso.server.domain.contest.util;

import com.gongjakso.server.domain.contest.entity.Contest;

public class ContestUtilTest {

    public static Contest buildContest() {
        return Contest.builder()
                .title("title")
                .body("description")
                .build();
    }
}
