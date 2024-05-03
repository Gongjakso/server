package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
public record GetContestRes (
    @Schema(
            description = "post가 생성된 순서를 관리하며 숫자가 높을수록 최신 공고임"
    )
    Long postId,

    @Schema(
            description = "post의 제목을 관리"
    )
    String title,

    @Schema(
            description = "post를 생성한 사용자의 이름"
    )
    String name, //팀장명

    @Schema(
            description = "post의 상태를 (RECRUITING | CANCEL | CLOSE | ACTIVE | COMPLETE) ENUM으로 관리",
            defaultValue = "RECRUITING",
            allowableValues = {"RECRUITING", "CANCEL", "CLOSE", "ACTIVE", "COMPLETE"}
    )
    PostStatus status,

    @Schema(
            description = "공모전/프로젝트 활동 시작 날짜 관리"
    )
    LocalDateTime startDate,

    @Schema(
            description = "공모전/프로젝트 활동 마감 날짜 관리"
    )
    LocalDateTime endDate,

    @Schema(
            description = "공모전/프로젝트 모집 마감 날짜 관리"
    )
    LocalDateTime finishDate,

    @Schema(
            description = "공모전/프로젝트 모집 마감 날짜를 디데이로 관리"
    )
    long daysRemaining,

    @Schema(
            description = "공고 카테고리(역할)를 (PLAN | DESIGN | FE | BE | ETC | LATER)의 ENUM으로 관리하는 테이블"
    )
    List<Category> categories,

    @Schema(
            description = "공고 스크랩 수, 스크랩 수가 높을수록 인기순 우선순위",
            defaultValue = "0"
    )
    long scrapCount
){ public static GetContestRes of(Post post){
        return GetContestRes.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .name(post.getMember().getName())
                .status(post.getStatus())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .daysRemaining(post.getFinishDate().isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), post.getFinishDate()))
                .categories(post.getCategories())
                .scrapCount(post.getScrapCount())
                .build();
    }
}
