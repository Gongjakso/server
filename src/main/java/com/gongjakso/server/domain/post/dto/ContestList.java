package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record ContestList(
        String title,
        String leader,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int restDate,
        int scrapNum,
        List<String> category
) {
    public static ContestList of(Post post,int restDate,List<String> category){
        return new ContestList(
                post.getTitle(),
                post.getMember().getName(),
                post.getStartDate(),
                post.getEndDate(),
                restDate,
                10,
                category
        );
    }
}
