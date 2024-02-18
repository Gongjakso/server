package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import lombok.Builder;

import java.util.List;

@Builder
public record ApplicationRes(
        Boolean is_decision,
        String application,
        String recruit_part,
        List<String> category,
        String recruit_role,
        List<String> stackName

) {
    public static ApplicationRes of(Apply apply, List<String> category,List<String> stackName){
        return new ApplicationRes(apply.getIsDecision(),apply.getApplication(), apply.getRecruit_part(), category, apply.getRecruit_role(), stackName);
    }
}
