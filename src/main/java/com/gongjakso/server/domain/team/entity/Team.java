package com.gongjakso.server.domain.team.entity;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.gongjakso.server.domain.member.entity.Member;
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

    private String meetingMethod;

    private String meetingArea;

    private LocalDate recruitFinishedAt;

    private LocalDate startedAt;

    private LocalDate finishedAt;

    private String channelLink;

    private int scrapCount;

    @Builder
    public Team(
            Long id
    ) {
        this.id = id;
    }
}