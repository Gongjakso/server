package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.CategoryType;
import com.gongjakso.server.domain.post.enumerate.PostStatus;

import java.time.LocalDateTime;

public record ParticipationList(
        String title,
        CategoryType recruit_part,
        String leaderName,
        LocalDateTime startDate,
        LocalDateTime finishDate,
        PostStatus postStatus,
        Boolean postType
) {
    public static ParticipationList of(Post post,CategoryType recruit_part) {
        return new ParticipationList(post.getTitle(), recruit_part,post.getMember().getName(),post.getStartDate(),post.getFinishDate(),post.getStatus(), post.isPostType());
    }
}
