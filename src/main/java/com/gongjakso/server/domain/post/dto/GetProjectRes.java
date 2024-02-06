package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class GetProjectRes {
    private Long postId;
    private String title;
    private String name; //팀장명
    private PostStatus status;
    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    private long daysRemaining;


    @Builder
    public GetProjectRes(Long postId, String title, String name, PostStatus status, LocalDateTime startDate,
                         LocalDateTime finishDate){
        this.postId = postId;
        this.title = title;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.daysRemaining = calculateDaysRemaining(finishDate);
    }
    private long calculateDaysRemaining(LocalDateTime finishDate) {
        LocalDateTime now = LocalDateTime.now();
        return daysRemaining = finishDate.isBefore(now) ? -1 : ChronoUnit.DAYS.between(now, finishDate);
    }

    // 파트명
    // 스크랩 횟수
}
