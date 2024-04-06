package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.StackType;
import jakarta.validation.constraints.Null;
import lombok.Builder;

import java.util.List;

@Builder
public record ApplicationRes(
        ApplyType applyType,
        String major,
        String application,
        String recruit_part,
        List<String> category,
        String recruit_role,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> postStack,
        @Null
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<StackType> myStack

) {
    public static ApplicationRes of(Apply apply, List<String> category,List<String> stackName){
        return new ApplicationRes(apply.getApplyType(),apply.getMember().getMajor(),apply.getApplication(), apply.getRecruit_part(), category, apply.getRecruit_role(), stackName,apply.getStackTypeList());
    }
}
