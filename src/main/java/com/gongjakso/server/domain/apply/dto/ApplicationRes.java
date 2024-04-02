package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import lombok.Builder;

import java.util.List;

@Builder
public record ApplicationRes(
        ApplyType applyType,
        String application,
        String recruit_part,
        List<String> category,
        String recruit_role,
        List<String> stackName

) {
    public static ApplicationRes of(Apply apply, List<String> category,List<String> stackName){
        return new ApplicationRes(apply.getApplyType(),apply.getApplication(), apply.getRecruit_part(), category, apply.getRecruit_role(), stackName);
    }
}
