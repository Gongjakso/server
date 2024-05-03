package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MyPageRes(
        Long postId,
        Long memberId,
        String memberName,
        String title,
        String contents,
        PostStatus status,
        ApplyType applyType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long daysRemaining,
        boolean postType,
        List<String> categoryList,
        Long scrapCount

) {
    public static MyPageRes of(Post post, Apply apply, List<String> categoryList) {
        return MyPageRes.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .memberName(post.getMember().getName())
                .title(post.getTitle())
                .contents(post.getContents())
                .status(post.getStatus())
                .applyType(apply.getApplyType())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .daysRemaining(post.getFinishDate().isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), post.getFinishDate()))
                .postType(post.isPostType())
                .categoryList(categoryList)
                .scrapCount(post.getScrapCount())
                .build();
    }
}

