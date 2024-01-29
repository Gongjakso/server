package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
public class PostReq {
    private String title;
    private String contents;
    private PostStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long maxPerson;
    private MeetingMethod meetingMethod;
    private String meetingArea;
    private boolean questionMethod;
    private String questionLink;
    private boolean isProject;

    /*
     * @Builder
     * public PostReq(String title, String contents, PostStatus status, LocalDateTime
     * startDate, LocalDateTime endDate,
     * Long maxPerson, PostType meetingMethod, String meetingArea, boolean
     * questionMethod, String questionLink, boolean isProject){
     * this.title = title;
     * this.contents = contents;
     * this.status = status;
     * this.startDate = startDate;
     * this.endDate = endDate;
     * this.maxPerson = maxPerson;
     * this.meetingArea = meetingArea;
     * this.meetingMethod = meetingMethod;
     * this.questionMethod = questionMethod;
     * this.questionLink = questionLink;
     * this.isProject = isProject;
     * }
     */
}