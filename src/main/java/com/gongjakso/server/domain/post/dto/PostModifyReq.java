package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record PostModifyReq (
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
        boolean postType
){
}
