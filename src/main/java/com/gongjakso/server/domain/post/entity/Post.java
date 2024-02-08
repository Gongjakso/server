package com.gongjakso.server.domain.post.entity;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "post")
@SQLDelete(sql="UPDATE post SET deleted_at = NOW() where post_id=?")
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

    @Column(name = "finish_date", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime finishDate;

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

    @Column(name = "post_type", nullable = false, columnDefinition = "tinyint")
    private boolean postType;



    @Builder
    public Post(String title, Member member, String contents, PostStatus status, LocalDateTime startDate,
                LocalDateTime endDate, LocalDateTime finishDate, Long maxPerson, MeetingMethod meetingMethod,
                String meetingArea, boolean questionMethod, String questionLink, boolean postType) {
        this.title = title;
        this.member = member;
        this.contents = contents;
        this.status = status;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.endDate = endDate;
        this.maxPerson = maxPerson;
        this.meetingMethod = meetingMethod;
        this.meetingArea = meetingArea;
        this.questionMethod = questionMethod;
        this.questionLink = questionLink;
        this.postType = postType;
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
        this.finishDate = req.getFinishDate();
        this.maxPerson = req.getMaxPerson();
        this.meetingMethod = req.getMeetingMethod();
        this.meetingArea = req.getMeetingArea();
        this.questionMethod = req.isQuestionMethod();
        this.questionLink = req.getQuestionLink();
        this.postType = req.isPostType();
    }

}