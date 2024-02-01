package com.gongjakso.server.domain.apply.dto;

public record PeriodReq(
        int addDateNum
) {
    public static PeriodReq of(int addDateNum){
        return new PeriodReq(addDateNum);
    }
}
