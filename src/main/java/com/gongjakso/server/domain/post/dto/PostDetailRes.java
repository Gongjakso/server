package com.gongjakso.server.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostDetailRes(
        Long postId,
        Long memberId,
        String memberName,
        String title,
        String contents,
        String urlLink,
        PostStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long daysRemaining,
        Long maxPerson,
        int currentPerson,
        List<StackName> stackNames,
        List<Category> categories,
        MeetingMethod meetingMethod,
        String meetingArea,
        boolean questionMethod,
        String questionLink,
        boolean postType,
        LocalDateTime createdAt,
        Long scrapCount
) {

    public static PostDetailRes of(Post post, int currentPerson) {
        return PostDetailRes.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .memberName(post.getMember().getName())
                .title(post.getTitle())
                .contents(post.getContents())
                .urlLink("https://www.naver.com")
                .status(post.getStatus())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .daysRemaining(post.getFinishDate().isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), post.getFinishDate()))
                .maxPerson(post.getMaxPerson())
                .currentPerson(currentPerson)
                .stackNames(post.getStackNames())
                .categories(post.getCategories())
                .meetingMethod(post.getMeetingMethod())
                .meetingArea(post.getMeetingArea())
                .questionMethod(post.isQuestionMethod())
                .questionLink(post.getQuestionLink())
                .postType(post.isPostType())
                .createdAt(post.getCreatedAt())
                .scrapCount(post.getScrapCount())
                .build();
    }
}
