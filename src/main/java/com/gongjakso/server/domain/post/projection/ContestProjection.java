package com.gongjakso.server.domain.post.projection;

import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.domain.post.enumerate.PostStatus;

import java.time.LocalDateTime;

public interface ContestProjection {
    Long getPostId();

    String getTitle();

    Long getMemberId();

    String getMemberName();

    PostStatus getStatus();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    LocalDateTime getFinishDate();

    long getDaysRemaining();

    Long getCategoryId();

    CategoryType getCategoryType();

    Integer getCategorySize();

    long getScrapCount();
}
