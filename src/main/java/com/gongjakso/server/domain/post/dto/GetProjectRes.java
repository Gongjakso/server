package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
public class GetProjectRes {
    private Long postId;
    private String title;
    private String name; //팀장명
    private PostStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private long daysRemaining;
    private List<Category> categories;
    private long scrapCount;

    @Builder
    public GetProjectRes(Long postId, String title, String name, PostStatus status, LocalDateTime startDate,
                         LocalDateTime endDate, LocalDateTime finishDate, List<Category> categories,long scrapCount ){
        this.postId = postId;
        this.title = title;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.daysRemaining = finishDate.isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), finishDate);
        this.categories = categories;
        this.scrapCount = scrapCount;
    }
}
