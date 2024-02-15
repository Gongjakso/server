package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetContestRes {
    private Long postId;
    private String title;
    private String name; //팀장명
    private PostStatus status;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private long daysRemaining;
    private List<Category> categories;
    private long scrapCount;

    @Builder
    public GetContestRes(Long postId, String title, String name, PostStatus status, LocalDateTime startDate,
                         LocalDateTime finishDate, List<Category> categories,long scrapCount){
        this.postId = postId;
        this.title = title;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.daysRemaining = getDaysRemaining();
        this.categories = categories;
        this.scrapCount = scrapCount;
    }
}
