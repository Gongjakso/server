package com.gongjakso.server.domain.contest.entity;

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
@Table(name = "contest")
@SQLDelete(sql = "UPDATE contest SET deleted_at = NOW() where contest_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "title",nullable = false,columnDefinition = "varchar(150)")
    private String title;

    @Column(name = "body",columnDefinition = "text")
    private String body;

    @Column(name = "contest_link",columnDefinition = "text")
    private String contestLink;

    @Column(name = "institution",columnDefinition = "varchar(100)")
    private String institution;

    @Column(name = "started_at",columnDefinition = "timestamp")
    private LocalDate startedAt;

    @Column(name = "finished_at",columnDefinition = "timestamp")
    private LocalDate finishedAt;

    @Column(name = "img_url",columnDefinition = "text")
    private String imgUrl;

//    public void update(UpdateContestDto contest,String imgUrl){
//        this.title= (contest.title()==null) ? this.title : contest.title();
//        this.body= (contest.body()==null) ? this.body : contest.body();
//        this.contestLink= (contest.contestLink()==null) ? this.contestLink : contest.contestLink();
//        this.institution= (contest.institution()==null) ? this.institution : contest.institution();
//        this.startedAt= (contest.startedAt()==null) ? this.startedAt : contest.startedAt();
//        this.finishedAt= (contest.finishedAt()==null) ? this.finishedAt : contest.finishedAt();
//        this.imgUrl= (imgUrl==null) ? this.imgUrl : imgUrl;
//    }

    @Builder
    public Contest(String title,String body,String contestLink,String institution,LocalDate startedAt,LocalDate finishedAt,String imgUrl){
        this.title=title;
        this.body=body;
        this.contestLink=contestLink;
        this.institution=institution;
        this.startedAt=startedAt;
        this.finishedAt=finishedAt;
        this.imgUrl=imgUrl;
    }
}