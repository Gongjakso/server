package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostRes(
        @Schema(
                description = "post가 생성된 순서를 관리하며 숫자가 높을수록 최신 공고임"
        )
        Long postId,

        @Schema(
                description = "member를 식별하는 pk값"
        )
        Long memberId,

        @Schema(
                description = "post의 제목을 관리"
        )
        String title,

        @Schema(
                description = "post의 내용을 관리"
        )
        String contents,

        @Schema(
                description = "공모전 공고에서 공모전의 출처를 관리"
        )
        String contestLink,

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
                description = "공모전/프로젝트 모집하려는 최대 인원 관리",
                defaultValue = "0"
        )
        Long maxPerson,

        @Schema(
                description = "사용 기술스택을 (REACT | TYPESCRIPT | JAVASCRIPT | NEXTJS | NODEJS | JAVA | SPRING | KOTLIN | SWIFT | FLUTTER | FIGMA | ETC)의 ENUM으로 관리하는 테이블"
        )
        List<StackName> stackNames,

        @Schema(
                description = "공고 카테고리(역할)를 (PLAN | DESIGN | FE | BE | ETC | LATER)의 ENUM으로 관리하는 테이블"
        )
        List<Category> categories,

        @Schema(
                description = "회의 방식을 (OFFLINE, ONLINE, BOTH) 로 관리",
                allowableValues = {"OFFLINE", "ONLINE", "BOTH"}
        )
        MeetingMethod meetingMethod,

        @Schema(
                description = "회의 장소를 행정구역단위로 관리"
        )
        String meetingCity,

        @Schema(
                description = "회의 장소를 행정구역단위로 관리"
        )
        String meetingTown,

        @Schema(
                description = "Q&A 방법 관리"
        )
        boolean questionMethod,

        @Schema(
                description = "Q&A Link 관리"
        )
        String questionLink,

        @Schema(
                description = "공고 타입을 0(contest), 1(project)으로 관리"
        )
        boolean postType,

        @Schema(
                description = "공고 생성 날짜 관리"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "공고 마지막 수정 날짜 관리"
        )
        LocalDateTime modifiedAt,

        @Schema(
                description = "공고 논리적 삭제 관리"
        )
        LocalDateTime deletedAt){
    public static PostRes of(Post post){
        return PostRes.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .title(post.getTitle())
                .contents(post.getContents())
                .contestLink(post.getContestLink())
                .status(post.getStatus())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .finishDate(post.getFinishDate())
                .maxPerson(post.getMaxPerson())
                .stackNames(post.getStackNames())
                .categories(post.getCategories())
                .meetingMethod(post.getMeetingMethod())
                .meetingCity(post.getMeetingCity())
                .meetingTown(post.getMeetingTown())
                .questionMethod(post.isQuestionMethod())
                .questionLink(post.getQuestionLink())
                .postType(post.isPostType())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    public boolean isPostType() {
        return postType;
    }
}