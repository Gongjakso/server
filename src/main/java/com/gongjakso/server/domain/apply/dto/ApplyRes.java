package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record ApplyRes(
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long max_person,
        int current_person,
        Boolean postType
) {
    public static ApplyRes of(Post post, int current_person){
        return new ApplyRes(post.getTitle(),post.getStartDate(),post.getEndDate(),post.getMaxPerson(),current_person,post.isPostType());
    }
}
