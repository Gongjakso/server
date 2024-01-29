package com.gongjakso.server.domain.post.entity;

import com.gongjakso.server.domain.post.enumerate.PostType;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false, columnDefinition = "bigint")
    private Long postId;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(20)")
    private String title;

    @Column(name = "contents", nullable = false, columnDefinition = "varchar(500)")
    private String contents;

    @Column(name = "status", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private PostType status;

    @Column(name = "start_date", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime endDate;

    @Column(name = "max_person", nullable = false, columnDefinition = "bigint")
    private Long maxPerson;

    @Column(name = "meeting_method", columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private PostType meetingMethod;

    @Column(name = "meeting_area", columnDefinition = "varchar(100)")
    private String meetingArea;

    @Column(name = "question_method", nullable = false, columnDefinition = "tinyint")
    private boolean questionMethod;

    @Column(name = "question_link", nullable = false, columnDefinition = "text")
    private String questionLink;

    @Column(name = "is_project", nullable = false, columnDefinition = "tinyint")
    private boolean isProject;

    @Builder
    public Post(Long postId, String title, String contents, PostType status, LocalDateTime startDate, LocalDateTime endDate, Long maxPerson, PostType meetingMethod, String meetingArea, boolean questionMethod, String questionLink, boolean isProject) {
        this.postId = postId;
        this.title = title;
        this.contents = contents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPerson = maxPerson;
        this.meetingMethod = meetingMethod;
        this.meetingArea = meetingArea;
        this.questionMethod = questionMethod;
        this.questionLink = questionLink;
        this.isProject = isProject;
    }
}