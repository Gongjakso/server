package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import jakarta.validation.constraints.Null;
import lombok.Builder;

import java.util.List;

@Builder
public record ApplicationRes(
        Long applyId,
        ApplyType applyType,
        String member_name,
        String major,
        String phone,
        String application,
        String recruit_part,
        List<String> category,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> postStack,
        @Null
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> applyStack

) {
    public static ApplicationRes of(Apply apply, List<String> category,List<String> stackName,List<String> applyStack){
        return new ApplicationRes(apply.getApplyId(), apply.getApplyType(),apply.getMember().getMajor(),apply.getApplication(), apply.getRecruit_part(), category, stackName,applyStack);
    }
}
