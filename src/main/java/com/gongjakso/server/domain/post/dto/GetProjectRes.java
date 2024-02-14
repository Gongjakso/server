package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
        this.daysRemaining = getDaysRemaining();
    }
    // 파트명
    // 스크랩 횟수
}
