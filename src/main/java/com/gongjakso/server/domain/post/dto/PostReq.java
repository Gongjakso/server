package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.enumerate.PostType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.springframework.cglib.core.Local;

@Data
@Getter
@NoArgsConstructor
public class PostReq {
    private String title;
    private String contents;
    private PostType status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long maxPerson;
    private PostType meetingMethod;
    private String meetingArea;
    private boolean questionMethod;
    private String questionLink;
    private boolean isProject;
}