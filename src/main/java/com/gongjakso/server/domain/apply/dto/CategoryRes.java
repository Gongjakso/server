package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.post.entity.StackName;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryRes(
        List<String> category,
        List<String> stackName
) {
    public static CategoryRes of(List<String> category,
                                 List<String> stackName){
        return new CategoryRes(category,stackName);
    }
}