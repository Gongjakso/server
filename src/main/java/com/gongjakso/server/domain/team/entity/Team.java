package com.gongjakso.server.domain.team.entity;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.dto.request.TeamReq;
import com.gongjakso.server.domain.team.enumerate.MeetingMethod;
import com.gongjakso.server.domain.team.enumerate.TeamStatus;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "team")
@SQLDelete(sql = "UPDATE team SET deleted_at = NOW() where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Contest contest;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(50)")
    private String title;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20)")
    private TeamStatus status;

    @Column(name = "total_count", nullable = false, columnDefinition = "int")
    private int totalCount;

    @Column(name = "pass_count", nullable = false, columnDefinition = "int")
    private int passCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_method", nullable = false, columnDefinition = "varchar(20)")
    private MeetingMethod meetingMethod;

    @Column(name = "province", columnDefinition = "varchar(20)")
    private String province; // 시/도 (ex. 서울특별시)

    @Column(name = "district", columnDefinition = "varchar(20)")
    private String district; // 시/군/구 (ex. 강남구)

    @Column(name = "recruit_finished_at", nullable = false, columnDefinition = "date")
    private LocalDate recruitFinishedAt;

    @Column(name = "started_at", columnDefinition = "date")
    private LocalDate startedAt;

    @Column(name = "finished_at", columnDefinition = "date")
    private LocalDate finishedAt;

    @Column(name = "channel_link", columnDefinition = "text")
    private String channelLink;

    @Column(name = "scrap_count", nullable = false, columnDefinition = "int")
    private int scrapCount;

    public void update(TeamReq teamReq) {
        this.title = (teamReq.title() != null) ? teamReq.title() : this.title;
        this.body = (teamReq.body() != null) ? teamReq.body() : this.body;
        this.totalCount = (teamReq.totalCount() != 0) ? teamReq.totalCount() : this.totalCount;
        this.meetingMethod = (teamReq.meetingMethod() != null) ? MeetingMethod.valueOf(teamReq.meetingMethod()) : this.meetingMethod;
        this.province = (teamReq.province() != null) ? teamReq.province() : this.province;
        this.district = (teamReq.district() != null) ? teamReq.district() : this.district;
        this.recruitFinishedAt = (teamReq.recruitFinishedAt() != null) ? teamReq.recruitFinishedAt() : this.recruitFinishedAt;
        this.startedAt = (teamReq.startedAt() != null) ? teamReq.startedAt() : this.startedAt;
        this.finishedAt = (teamReq.finishedAt() != null) ? teamReq.finishedAt() : this.finishedAt;
        this.channelLink = (teamReq.channelLink() != null) ? teamReq.channelLink() : this.channelLink;
    }

    @Builder
    public Team(Member member,
                Contest contest,
                String title,
                String body,
                int totalCount,
                MeetingMethod meetingMethod,
                String province,
                String district,
                LocalDate recruitFinishedAt,
                LocalDate startedAt,
                LocalDate finishedAt,
                String channelLink) {
        this.member = member;
        this.contest = contest;
        this.title = title;
        this.body = body;
        this.status = TeamStatus.ACTIVE;
        this.totalCount = totalCount;
        this.passCount = 0;
        this.meetingMethod = meetingMethod;
        this.province = province;
        this.district = district;
        this.recruitFinishedAt = recruitFinishedAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.channelLink = channelLink;
        this.scrapCount = 0;
    }
}