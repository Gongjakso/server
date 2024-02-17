package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Post;

import java.time.LocalDateTime;

public record ScrapPost(
        long postId,
        String title,
        LocalDateTime finishDate,
        Boolean postType
) {
    public static ScrapPost of(Post post){
        return new ScrapPost(post.getPostId(),post.getTitle(),post.getFinishDate(),post.isPostType());
    }
}
