package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Post;
import lombok.Builder;

@Builder
public record GetPostRelation(
        String status
) {
    public static GetPostRelation of(String status) {
        return GetPostRelation.builder()
                .status(status)
                .build();
    }
}
