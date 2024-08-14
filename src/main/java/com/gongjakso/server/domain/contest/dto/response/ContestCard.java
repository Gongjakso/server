package com.gongjakso.server.domain.contest.dto.response;

import com.gongjakso.server.domain.contest.entity.Contest;
import lombok.Builder;

import static com.gongjakso.server.domain.contest.dto.response.ContestRes.text;

@Builder
public record ContestCard(
        long id,
        String title,
        String imgUrl,
        String dayState,
        int teamCount
) {
    public static ContestCard of(Contest contest,int teamCount){
        return ContestCard.builder()
                .id(contest.getId())
                .title(contest.getTitle())
                .imgUrl(contest.getImgUrl())
                .dayState(text(contest.getStartedAt(),contest.getFinishedAt()))
                .teamCount(teamCount)
                .build();
    }
}
