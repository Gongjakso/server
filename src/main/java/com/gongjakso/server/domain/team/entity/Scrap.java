package com.gongjakso.server.domain.team.entity;

import com.gongjakso.server.domain.member.entity.Member;
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
@Table(name = "scrap")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// Scrap 테이블은 물리적 삭제 방식
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "scrapped_at", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime scrappedAt;

    @Builder
    public Scrap(Member member, Team team){
        this.member = member;
        this.team = team;
        this.scrappedAt = LocalDateTime.now();
    }
}
