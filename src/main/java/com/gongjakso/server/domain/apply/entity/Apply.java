package com.gongjakso.server.domain.apply.entity;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.apply.enumerate.ApplyStatus;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Table(name = "apply")
@SQLDelete(sql = "UPDATE apply SET deleted_at = NOW() where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apply extends BaseTimeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, columnDefinition = "bigint")
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id", nullable = false, columnDefinition = "bigint")
        private Team team;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="member_id", nullable = false, columnDefinition = "bigint")
        private Member member;

        @Embedded
        private PortfolioInfo portfolioInfo;

        @Column(nullable = false, columnDefinition = "text")
        private String body;

        @Column(nullable = false, columnDefinition = "varchar(20)")
        @Enumerated(EnumType.STRING)
        private ApplyStatus status;

        @Column(nullable = false, columnDefinition = "varchar(20)")
        private String part;

        @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
        private boolean isViewed;

        @Builder
        public Apply(Team team, Member member, PortfolioInfo portfolioInfo, String body, ApplyStatus status, String part) {
                this.team = team;
                this.member = member;
                this.portfolioInfo = portfolioInfo;
                this.body = body;
                this.status = status;
                this.part = part;
                this.isViewed = false;
        }

        public void select(ApplyStatus status) {
                this.status = status;
        }

        public void setViewed() {
                this.isViewed = true;
        }
}
