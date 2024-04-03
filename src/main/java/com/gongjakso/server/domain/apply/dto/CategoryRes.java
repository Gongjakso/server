package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.post.entity.StackName;

import java.util.List;
public record CategoryRes(
        List<String> category,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> stackName
) {
    public static CategoryRes of(List<String> category,
                                 List<String> stackName){
        return new CategoryRes(category,stackName);
    }
}