package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ScrapPost(
        @Schema(
                description = "post가 생성된 순서를 관리하며 숫자가 높을수록 최신 공고임"
        )
        long postId,

        @Schema(
                description = "post의 제목을 관리"
        )
        String title,

        @Schema(
                description = "공모전/프로젝트 활동 마감 날짜 관리"
        )
        LocalDateTime endDate,

        @Schema(
                description = "공고 타입을 0(contest), 1(project)으로 관리"
        )
        Boolean postType
) {
    public static ScrapPost of(Post post){
        return new ScrapPost(post.getPostId(),post.getTitle(),post.getEndDate(),post.isPostType());
    }
}