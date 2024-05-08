package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
public record CategoryRes(
        List<String> category,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> stack
) {
    public static CategoryRes of(List<String> category,
                                 List<String> stack){
        return new CategoryRes(category,stack);
    }
}