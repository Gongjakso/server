package com.gongjakso.server.domain.contest.util;

import com.gongjakso.server.domain.contest.dto.request.ContestReq;
import com.gongjakso.server.domain.contest.entity.Contest;

import java.time.LocalDate;

public class ContestUtilTest {
    public static ContestReq buildContest(){
        return ContestReq.builder()
                .title("공모전 title")
                .body("공모전 본문이에요")
                .contestLink("http://gongjakso.")
                .institution("공작소")
                .startedAt(LocalDate.of(2024,8,20))
                .finishedAt(LocalDate.of(2024,9,28))
                .build();
    }

    public static ContestReq buildContestReq(){
        return ContestReq.builder()
                .title("공모전 title 수정")
                .body("공모전 body 수정")
                .contestLink("http://")
                .institution("공작소 수정")
                .startedAt(LocalDate.of(2024,9,20))
                .finishedAt(LocalDate.of(2024,10,28))
                .build();
    }

}
