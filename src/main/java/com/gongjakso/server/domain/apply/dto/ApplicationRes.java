package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import lombok.Builder;

@Builder
public record ApplicationRes(
        String application,
        String recruit_part
//        String[] category
) {
    public static ApplicationRes of(Apply apply){
        return new ApplicationRes(apply.getApplication(), apply.getRecruit_part());
    }
}
