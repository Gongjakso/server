package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class PostReq {
    private String title;
    private String contents;
    private String contestLink;
    private PostStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime finishDate;
    private Long maxPerson;
    private List<StackName> stackNames;
    private List<Category> categories;
    private MeetingMethod meetingMethod;
    private String meetingArea;
    private boolean questionMethod;
    private String questionLink;
    private boolean postType;

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