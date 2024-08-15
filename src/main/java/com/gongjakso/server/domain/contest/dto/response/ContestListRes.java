package com.gongjakso.server.domain.contest.dto.response;

import com.gongjakso.server.domain.contest.entity.Contest;
import lombok.Builder;

import java.util.List;

@Builder
public record ContestListRes(
        List<ContestCard> contestList,
        long total,
        int totalPages
) {
    public static ContestListRes of(List<ContestCard> contestList,
                                    long total,
                                    int totalPages){
        return ContestListRes.builder()
                .contestList(contestList)
                .total(total)
                .totalPages(totalPages)
                .build();
    }
}
