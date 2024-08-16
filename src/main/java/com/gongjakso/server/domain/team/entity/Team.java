package com.gongjakso.server.domain.team.entity;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.member.entity.Member;
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

    private String body;

    private TeamStatus status;

    private int totalCount;

    private int passCount;

    private MeetingMethod meetingMethod;

    private String province; // 시/도 (ex. 서울특별시)

    private String district; // 시/군/구 (ex. 강남구)

    private LocalDate recruitFinishedAt;

    private LocalDate startedAt;

    private LocalDate finishedAt;

    private String channelLink;

    private int scrapCount;

    @Builder
    public Team(Long id,
                Member member,
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
        this.id = id;
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