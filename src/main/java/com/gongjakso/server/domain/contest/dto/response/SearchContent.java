package com.gongjakso.server.domain.contest.dto.response;

import com.gongjakso.server.domain.contest.entity.Contest;
import lombok.Builder;

import java.util.List;

@Builder
public record SearchContent(
        List<Contest> contestList,
        long total,
        int totalPages
) {
    public static SearchContent of(List<Contest> contestList,
                                   long total,int totalPages){
        return SearchContent.builder()
                .contestList(contestList)
                .total(total)
                .totalPages(totalPages)
                .build();
    }
}
