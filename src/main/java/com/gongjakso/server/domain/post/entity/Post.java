package com.gongjakso.server.domain.post.entity;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.PostModifyReq;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "post")
@SQLDelete(sql="UPDATE post SET deleted_at = NOW() where post_id=?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false, columnDefinition = "bigint")
    private Long postId;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(40)")
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StackName> stackNames = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    @Column(name = "meeting_method", columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private MeetingMethod meetingMethod;

    @Column(name = "meeting_city", columnDefinition = "varchar(100)")
    private String meetingCity;

    @Column(name = "meeting_town", columnDefinition = "varchar(100)")
    private String meetingTown;

    @Column(name = "constest_link", columnDefinition = "varchar(100)")
    private String contestLink;

    @Column(name = "question_method", nullable = false, columnDefinition = "tinyint")
    private boolean questionMethod;

    @Column(name = "question_link", nullable = false, columnDefinition = "text")
    private String questionLink;

    @Column(name = "post_type", nullable = false, columnDefinition = "tinyint")
    private boolean postType;

    @Column(name = "days_remaining", nullable = false, columnDefinition = "bigint")
    private long daysRemaining;

    @Column(name="scrap_count", nullable = false, columnDefinition = "bigint")
    private long scrapCount;

    @Column(name="post_view", nullable = false, columnDefinition = "bigint default 0")
    private Long postView;

    public long getDaysRemaining(){
        return finishDate.isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), finishDate);
    }

    @Builder
    public Post(String title, Member member, String contents, String contestLink, LocalDateTime startDate,
                LocalDateTime endDate, LocalDateTime finishDate, Long maxPerson, MeetingMethod meetingMethod,
                String meetingCity, String meetingTown, boolean questionMethod, String questionLink, boolean postType,
                List<StackName> stackNames, List<Category> categories) {
        this.title = title;
        this.member = member;
        this.contents = contents;
        this.contestLink = contestLink;
        this.status = PostStatus.RECRUITING;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.endDate = endDate;
        this.maxPerson = maxPerson;
        this.meetingMethod = meetingMethod;
        this.meetingCity = meetingCity;
        this.meetingTown = meetingTown;
        this.questionMethod = questionMethod;
        this.questionLink = questionLink;
        this.postType = postType;
        this.daysRemaining = finishDate.isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), finishDate);
        this.stackNames = stackNames;
        this.categories = categories;
        this.postView = 0L;
    }

    public void modify(PostModifyReq req) {
        this.title = req.title();
        this.contents = req.contents();
        this.contestLink = req.contestLink();
        this.status = (this.finishDate.isEqual(req.finishDate())) ? req.status() : PostStatus.EXTENSION;
        this.startDate = req.startDate();
        this.endDate = req.endDate();
        this.finishDate = req.finishDate();
        this.maxPerson = req.maxPerson();
        this.meetingMethod = req.meetingMethod();
        this.meetingCity = req.meetingCity();
        this.meetingTown = req.meetingTown();
        this.questionMethod = req.questionMethod();
        this.questionLink = req.questionLink();
        this.postType = req.postType();
        this.daysRemaining = finishDate.isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), finishDate);
    }

    public void updatePostView(Long postView) {
        this.postView = postView + 1;
    }

    public void updateStatus(PostStatus status) {
        this.status = status;
    }

    @Builder
    public Post(Long id, String title, Member member, String contents, String contestLink, LocalDateTime startDate,
                LocalDateTime endDate, LocalDateTime finishDate, Long maxPerson, MeetingMethod meetingMethod,
                String meetingCity, String meetingTown, boolean questionMethod, String questionLink, boolean postType,
                List<StackName> stackNames, List<Category> categories) {
        this.postId = id;
        this.title = title;
        this.member = member;
        this.contents = contents;
        this.contestLink = contestLink;
        this.status = PostStatus.RECRUITING;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.endDate = endDate;
        this.maxPerson = maxPerson;
        this.meetingMethod = meetingMethod;
        this.meetingCity = meetingCity;
        this.meetingTown = meetingTown;
        this.questionMethod = questionMethod;
        this.questionLink = questionLink;
        this.postType = postType;
        this.daysRemaining = finishDate.isBefore(LocalDateTime.now()) ? -1 : ChronoUnit.DAYS.between(LocalDateTime.now(), finishDate);
        this.stackNames = stackNames;
        this.categories = categories;
        this.postView = 0L;
    }
}