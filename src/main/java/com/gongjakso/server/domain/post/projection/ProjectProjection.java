package com.gongjakso.server.domain.post.projection;

import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.domain.post.enumerate.PostStatus;

import java.time.LocalDateTime;

public interface ProjectProjection {
    Long getPostId();

    String getTitle();

    String getName();

    PostStatus getStatus();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    LocalDateTime getFinishDate();

    long getDaysRemaining();

    Long getCategoryId();

    Long getCategoryPostId();

    CategoryType getCategoryType();

    Integer getSize();

    long getScrapCount();
}
