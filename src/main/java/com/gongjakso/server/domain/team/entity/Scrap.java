package com.gongjakso.server.domain.team.entity;

import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Table(name = "scrap")
@SQLDelete(sql = "UPDATE scrap SET deleted_at = NOW() where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;


    @Builder
    public Scrap(Team team){
        this.team = team;
    }
}
