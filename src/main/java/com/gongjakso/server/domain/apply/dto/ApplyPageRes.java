package com.gongjakso.server.domain.apply.dto;

import java.util.List;

public record ApplyPageRes(
        List<ApplyList> applyLists,
        int pageNo,
        int pageSize,
        int totalPages,
        boolean last
) {

    public static ApplyPageRes of(List<ApplyList> applyLists, int pageNo, int pageSize, int totalPages,boolean last) {
        return new ApplyPageRes(applyLists, pageNo, pageSize, totalPages, last);
    }
}
