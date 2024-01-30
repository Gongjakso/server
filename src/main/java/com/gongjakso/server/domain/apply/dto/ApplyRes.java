package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.post.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ApplyRes(
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long max_person,
        int current_person,
        List<ApplyList> apply_list
) {
    public static ApplyRes of(Post post, int current_person,List<ApplyList> apply_list){
        return new ApplyRes(post.getStartDate(),post.getEndDate(),post.getMaxPerson(),current_person,apply_list);
    }
}
