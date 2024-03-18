package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostRes(Long postId, Long memberId, String title, String contents, String contestLink, PostStatus status, LocalDateTime startDate,
                      LocalDateTime endDate, LocalDateTime finishDate, Long maxPerson, List<StackName> stackNames,
                      List<Category> categories, MeetingMethod meetingMethod, String meetingCity, String meetingTown,
                      boolean questionMethod, String questionLink, boolean postType, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt){
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