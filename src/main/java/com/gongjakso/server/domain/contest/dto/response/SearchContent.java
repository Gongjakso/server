package com.gongjakso.server.domain.contest.dto.response;

import com.gongjakso.server.domain.contest.entity.Contest;
import lombok.Builder;

import java.util.List;

@Builder
public record SearchContent(
        List<Contest> contestList,
        long pageSize
) {
    public static SearchContent of(List<Contest> contestList,
                                   long pageSize){
        return SearchContent.builder()
                .contestList(contestList)
                .pageSize(pageSize)
                .build();
    }
}
