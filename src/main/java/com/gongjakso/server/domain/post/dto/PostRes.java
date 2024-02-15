package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostRes {
    private Long postId;
    private Long memberId;
    private String title;
    private String contents;
    private PostStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime finishDate;
    private Long maxPerson;
    private List<StackName> stackNames;
    private MeetingMethod meetingMethod;
    private String meetingArea;
    private boolean questionMethod;
    private String questionLink;
    private boolean postType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    @Builder
    public PostRes(Long postId, Long memberId, String title, String contents, PostStatus status, LocalDateTime startDate, LocalDateTime endDate,
                   LocalDateTime finishDate, Long maxPerson, List<StackName> stackNames, MeetingMethod meetingMethod, String meetingArea, boolean questionMethod, String questionLink,
                   boolean postType, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.postId = postId;
        this.memberId = memberId;
        this.title = title;
        this.contents = contents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.finishDate = finishDate;
        this.maxPerson = maxPerson;
        this.stackNames = stackNames;
        this.meetingArea = meetingArea;
        this.meetingMethod = meetingMethod;
        this.questionMethod = questionMethod;
        this.questionLink = questionLink;
        this.postType = postType;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }
}