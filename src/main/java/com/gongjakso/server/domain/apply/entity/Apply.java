package com.gongjakso.server.domain.apply.entity;

import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "apply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id",nullable = false,columnDefinition = "bigint")
    private Long applyId;

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

    @Column(name = "recruit_role",columnDefinition = "varchar(50)")
    private String recruit_role;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Enumerated(EnumType.STRING)
    private ApplyType applyType;

    @Builder
    public Apply(Long applyId, Member member,Post post, String application,String recruit_part,String recruit_role,PostType type, ApplyType applyType){
        this.applyId=applyId;
        this.member=member;
        this.post=post;
        this.application=application;
        this.recruit_part=recruit_part;
        this.recruit_role=recruit_role;
        this.type=type;
        this.applyType=applyType;
    }
}
