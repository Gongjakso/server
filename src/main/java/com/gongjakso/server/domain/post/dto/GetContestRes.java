package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
public record GetContestRes (
    Long postId,
    String title,
    String name, //팀장명
    PostStatus status,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime finishDate,
    long daysRemaining,
    List<Category> categories,
    long scrapCount
){ public static GetContestRes of(Post post){
        return GetContestRes.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .name(post.getMember().getName())
                .status(post.getStatus())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .daysRemaining(post.getFinishDate().isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), post.getFinishDate()))
                .categories(post.getCategories())
                .scrapCount(post.getScrapCount())
                .build();
    }
}
