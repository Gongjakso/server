package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.PostType;

public record AddApplyReq(
        String application,
        String recruit_part,
        PostType type
) {
    public Apply toEntity(){
        return Apply.builder()
                .application(application)
                .recruit_part(recruit_part)
                .type(type)
                .build();
    }
}
