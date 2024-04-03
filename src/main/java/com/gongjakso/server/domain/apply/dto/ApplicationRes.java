package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.StackType;
import lombok.Builder;

import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public record ApplicationRes(
        ApplyType applyType,
        String major,
        String application,
        String recruit_part,
        List<String> category,
        String recruit_role,
        List<String> stackName,
        List<StackType> myStackName

) {
    public static ApplicationRes of(Apply apply, List<String> category,List<String> stackName){
        return new ApplicationRes(apply.getApplyType(),apply.getMember().getMajor(),apply.getApplication(), apply.getRecruit_part(), category, apply.getRecruit_role(), stackName,apply.getStackTypeList());
    }
}
