package com.gongjakso.server.domain.post.entity;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false, columnDefinition = "bigint")
    private Long postId;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(20)")
    private String title;

    @Column(name = "contents", nullable = false, columnDefinition = "varchar(500)")
    private String contents;

    @Column(name = "status", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(name = "start_date", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime endDate;

    @Column(name = "max_person", nullable = false, columnDefinition = "bigint")
    private Long maxPerson;

    @Column(name = "meeting_method", columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private MeetingMethod meetingMethod;

    @Column(name = "meeting_area", columnDefinition = "varchar(100)")
    private String meetingArea;

    @Column(name = "question_method", nullable = false, columnDefinition = "tinyint")
    private boolean questionMethod;

    @Column(name = "question_link", nullable = false, columnDefinition = "text")
    private String questionLink;

    @Column(name = "is_project", nullable = false, columnDefinition = "tinyint")
    private boolean isProject;

    @Builder
    public Post(Member member, PostReq req) {
        this.title = req.getTitle();
        this.member = member;
        this.contents = req.getContents();
        this.status = req.getStatus();
        this.startDate = req.getStartDate();
        this.endDate = req.getEndDate();
        this.maxPerson = req.getMaxPerson();
        this.meetingMethod = req.getMeetingMethod();
        this.meetingArea = req.getMeetingArea();
        this.questionMethod = req.isQuestionMethod();
        this.questionLink = req.getQuestionLink();
        this.isProject = req.isProject();
    }

    public void read(PostReq req) {
        this.title = req.getTitle();
        this.contents = req.getContents();
        this.status = req.getStatus();
        this.startDate = req.getStartDate();
        this.endDate = req.getEndDate();
        this.maxPerson = req.getMaxPerson();
        this.meetingMethod = req.getMeetingMethod();
        this.meetingArea = req.getMeetingArea();
        this.questionMethod = req.isQuestionMethod();
        this.questionLink = req.getQuestionLink();
        this.isProject = req.isProject();
    }

    public void modify(PostReq req) {
        this.title = req.getTitle();
        this.contents = req.getContents();
        this.status = req.getStatus();
        this.startDate = req.getStartDate();
        this.endDate = req.getEndDate();
        this.maxPerson = req.getMaxPerson();
        this.meetingMethod = req.getMeetingMethod();
        this.meetingArea = req.getMeetingArea();
        this.questionMethod = req.isQuestionMethod();
        this.questionLink = req.getQuestionLink();
        this.isProject = req.isProject();
    }
}