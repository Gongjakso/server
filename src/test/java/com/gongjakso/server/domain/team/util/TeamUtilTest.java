package com.gongjakso.server.domain.team.util;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.team.entity.Team;

public class TeamUtilTest {

    public static Team buildteam() {
        return Team.builder()
                .contest(Contest.builder().build())
                .build();
    }
}
