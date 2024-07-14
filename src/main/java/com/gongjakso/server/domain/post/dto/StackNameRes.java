package com.gongjakso.server.domain.post.dto;

import lombok.Builder;

@Builder
public record StackNameRes(
        Long stackNameId,

        String stackNameType
) {

}
