package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApplyRes(
        String title,
        String member_name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long total_person,
        int current_person,
        Long max_person,
        Boolean postType,
        List<String> category
) {
    public static ApplyRes of(Post post, int current_person, List<String> category){
        return new ApplyRes(post.getTitle(),post.getMember().getName(), post.getStartDate(),post.getEndDate(),(post.getMaxPerson()+1),current_person,post.getMaxPerson(),post.isPostType(), category);
    }
}
