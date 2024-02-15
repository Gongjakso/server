package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.apply.dto.ApplyList;

import java.util.List;

public record ContestPageRes(
        List<ContestList> contestLists,
        int pageNo,
        int pageSize,
        int totalPages,
        boolean last
) {

    public static ContestPageRes of(List<ContestList> contestLists, int pageNo, int pageSize, int totalPages, boolean last) {
        return new ContestPageRes(contestLists, pageNo, pageSize, totalPages, last);
    }
}
