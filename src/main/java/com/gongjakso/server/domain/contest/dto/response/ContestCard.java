package com.gongjakso.server.domain.contest.dto.response;

import com.gongjakso.server.domain.contest.entity.Contest;
import lombok.Builder;

@Builder
public record ContestCard(
        long id,
        String title,
        String imgUrl,
        int teamCount
) {
    public static ContestCard of(Contest contest,int teamCount){
        return ContestCard.builder()
                .id(contest.getId())
                .title(contest.getTitle())
                .imgUrl(contest.getImgUrl())
                .teamCount(teamCount)
                .build();
    }
}
