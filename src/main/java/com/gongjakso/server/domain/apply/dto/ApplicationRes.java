package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import lombok.Builder;

@Builder
public record ApplicationRes(
        Boolean is_decision,
        String application,
        String recruit_part
//        String[] category
) {
    public static ApplicationRes of(Apply apply){
        return new ApplicationRes(apply.getIs_decision(),apply.getApplication(), apply.getRecruit_part());
    }
}
