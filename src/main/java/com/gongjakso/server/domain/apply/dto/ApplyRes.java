package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApplyRes(
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long max_person,
        int current_person,
        int pass_person,
        Boolean postType,
        List<String> category,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> stack
) {
    public static ApplyRes of(Post post, int current_person, List<String> category, List<String> stack){
        return new ApplyRes(post.getTitle(),post.getStartDate(),post.getEndDate(),(post.getMaxPerson()+1),(current_person+1),current_person,post.isPostType(), category, stack);
    }
}
