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
        String memberName,
        String major,
        String phone,
        String application,
        String recruitPart,
        List<String> category,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> postStack,
        @Null
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> applyStack

) {
    public static ApplicationRes of(Apply apply, List<String> category, List<String> stackName, List<String> applyStack) {
        return ApplicationRes.builder()
                .applyId(apply.getApplyId())
                .applyType(apply.getApplyType())
                .memberName(apply.getMember().getName())
                .major(apply.getMember().getMajor())
                .phone(apply.getMember().getPhone())
                .application(apply.getApplication())
                .recruitPart(apply.getRecruit_part())
                .category(category)
                .postStack(stackName)
                .applyStack(applyStack)
                .build();
    }
}
