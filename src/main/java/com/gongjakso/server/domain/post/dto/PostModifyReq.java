package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PostModifyReq (
        String title,
        String contents,
        String contestLink,
        PostStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime finishDate,
        Long maxPerson,
        List<StackName> stackNames,
        List<Category> categories,
        MeetingMethod meetingMethod,
        String meetingCity,
        String meetingTown,
        boolean questionMethod,
        String questionLink,
        boolean postType
){
}
