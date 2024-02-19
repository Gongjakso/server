package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record PostReq (
        String title,
        String contents,
        String contestLink,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime finishDate,
        Long maxPerson,
        List<StackName> stackNames,
        List<Category> categories,
        MeetingMethod meetingMethod,
        String meetingArea,
        boolean questionMethod,
        String questionLink,
        boolean postType
){
    @Builder
    public Post from(){
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .contestLink(this.contestLink)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .finishDate(this.finishDate)
                .maxPerson(this.maxPerson)
                .meetingMethod(this.meetingMethod)
                .meetingArea(this.meetingArea)
                .questionMethod(this.questionMethod)
                .questionLink(this.questionLink)
                .postType(this.postType)
                .build();
    }
}