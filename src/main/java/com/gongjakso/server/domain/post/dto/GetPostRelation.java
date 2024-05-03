package com.gongjakso.server.domain.post.dto;

import lombok.Builder;

@Builder
public record GetPostRelation(
        String role
) {
    public static GetPostRelation of(String role) {
        return GetPostRelation.builder()
                .role(role)
                .build();
    }
}
