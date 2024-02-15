package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.post.entity.Post;

import java.util.List;

public record PageRes(
        List<ApplyList> applyLists,
        int pageNo,
        int pageSize,
        int totalPages,
        boolean last
) {

    public static PageRes of(List<ApplyList> applyLists, int pageNo, int pageSize, int totalPages,boolean last) {
        return new PageRes(applyLists, pageNo, pageSize, totalPages, last);
    }
}
