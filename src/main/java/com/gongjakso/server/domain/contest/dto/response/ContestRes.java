package com.gongjakso.server.domain.contest.dto.response;

import com.gongjakso.server.domain.contest.entity.Contest;
import lombok.Builder;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Builder
public record ContestRes(
        String title,
        String body,
        String contestLink,
        String institution,
        LocalDate startedAt,
        LocalDate finishedAt,
        String dayState,
        String imgUrl
) {
    public static String text(LocalDate startedAt, LocalDate finishedAt){
        long remainDay = ChronoUnit.DAYS.between(startedAt,finishedAt);
        if(remainDay==0){
            return "오늘 마감";
        }else if(remainDay<0){
            return "공모전 모집 종료";
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
                .dayState(text(contest.getStartedAt(),contest.getFinishedAt()))
                .build();
    }
}
