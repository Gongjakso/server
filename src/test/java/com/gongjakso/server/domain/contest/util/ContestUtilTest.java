package com.gongjakso.server.domain.contest.util;


import com.gongjakso.server.domain.contest.dto.request.ContestReq;
import com.gongjakso.server.domain.contest.dto.request.UpdateContestDto;
import com.gongjakso.server.domain.contest.dto.response.ContestRes;
import com.gongjakso.server.domain.contest.entity.Contest;

import java.time.LocalDate;

public class ContestUtilTest {
    public static Contest buildContest() {
        return Contest.builder()
                .title("공모전 title")
                .body("공모전 본문이에요")
                .contestLink("http://gongjakso")
                .institution("공작소")
                .startedAt(LocalDate.now())
                .finishedAt(LocalDate.now().plusDays(1))
                .build();
    }

    public static ContestReq buildContestReq() {
        return ContestReq.builder()
                .title("공모전 title")
                .body("공모전 body")
                .contestLink("http://gongjakso")
                .institution("공작소")
                .startedAt(LocalDate.now())
                .finishedAt(LocalDate.now().plusDays(1))
                .build();
    }

    public static UpdateContestDto buildUpdateContestDto() {
        return UpdateContestDto.builder()
                .title("공모전 title")
                .body("공모전 body")
                .contestLink("http://gongjakso")
                .institution("공작소")
                .startedAt(LocalDate.now())
                .finishedAt(LocalDate.now().plusDays(1))
                .build();
    }

    public static ContestRes buildContestResDto(){
        return ContestRes.builder()
                .title("공모전 title")
                .body("공모전 body")
                .contestLink("http://gongjakso")
                .institution("공작소")
                .startedAt(LocalDate.now())
                .finishedAt(LocalDate.now().plusDays(1))
                .build();
    }

}
