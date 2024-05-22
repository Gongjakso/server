package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostSimpleRes(
        String title,
        String leaderName,
        LocalDateTime startDate,
        LocalDateTime finishDate,
        PostStatus postStatus,
        Boolean postType
) {
    public static PostSimpleRes of(Post post) {
        return PostSimpleRes.builder()
                .title(post.getTitle())
                .leaderName(post.getMember().getName())
                .startDate(post.getStartDate())
                .finishDate(post.getFinishDate())
                .postStatus(post.getStatus())
                .postType(post.isPostType())
                .build();
    }
}
