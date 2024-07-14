package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.enumerate.CategoryType;
import lombok.Builder;

@Builder
public record CategoryRes(
        Long categoryId,

        CategoryType categoryType,

        Integer size
) {
}
