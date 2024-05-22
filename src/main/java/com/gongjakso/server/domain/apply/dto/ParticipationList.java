package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ParticipationList(
        Long postId,
        String title,
        String leaderName,
        LocalDateTime startDate,
        LocalDateTime finishDate,
        PostStatus postStatus,
        Boolean postType
) {
    public static ParticipationList of(Post post) {
        return ParticipationList.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .leaderName(post.getMember().getName())
                .startDate(post.getStartDate())
                .finishDate(post.getFinishDate())
                .postStatus(post.getStatus())
                .postType(post.isPostType())
                .build();
    }
}
