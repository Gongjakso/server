package com.gongjakso.server.domain.apply.dto;

import java.util.List;

public record ParticipationPageRes(
        List<ParticipationList> participationLists,
        int pageNo,
        int pageSize,
        int totalPages,
        boolean last
) {
    public static ParticipationPageRes of(List<ParticipationList> participationLists, int pageNo, int pageSize, int totalPages,boolean last) {
        return new ParticipationPageRes(participationLists, pageNo, pageSize, totalPages, last);
    }
}
