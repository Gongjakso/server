package com.gongjakso.server.domain.apply.entity;

import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "apply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id",nullable = false,columnDefinition = "bigint")
    private Long listID;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "application",nullable = false,columnDefinition = "varchar(500)")
    private String application;

    @Column(name = "recruit_part",nullable = false,columnDefinition = "varchar(50)")
    private String recruit_part;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(name = "is_pass", columnDefinition = "boolean" )
    private Boolean is_pass;

    @Column(name = "is_open", columnDefinition = "boolean" )
    private Boolean is_open;

    @Builder
    public Apply(Long listID, Member member,Post post, String application,String recruit_part,PostType type, Boolean is_pass,Boolean is_open){
        this.listID=listID;
        this.member=member;
        this.post=post;
        this.application=application;
        this.recruit_part=recruit_part;
        this.type=type;
        this.is_pass=is_pass;
        this.is_open=is_open;
    }
}
