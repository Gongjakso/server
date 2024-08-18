package com.gongjakso.server.domain.contest.dto.response;

import com.gongjakso.server.domain.contest.entity.Contest;
import lombok.Builder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Builder
public record ContestRes(
        String title,
        String body,
        String contestLink,
        String institution,
        LocalDate startedAt,
        LocalDate finishedAt,
        String dayState,
        String imgUrl,
        int viewCount
) {
    public static String text(LocalDate finishedAt){
        long remainDay = ChronoUnit.DAYS.between(LocalDate.now(),finishedAt);
        if(remainDay==0){
            return "오늘 모집 마감";
        }else if(remainDay<0){
            return "공고 종료";
        }else{
            return "D-"+remainDay;
        }
    }
    public static ContestRes of(Contest contest){
        return ContestRes.builder()
                .title(contest.getTitle())
                .body(contest.getBody())
                .contestLink(contest.getContestLink())
                .institution(contest.getInstitution())
                .startedAt(contest.getStartedAt())
                .finishedAt(contest.getFinishedAt())
                .imgUrl(contest.getImgUrl())
                .dayState(text(contest.getFinishedAt()))
                .viewCount(contest.getView())
                .build();
    }
}
