package com.gongjakso.server.domain.member.entity;

import com.gongjakso.server.domain.member.dto.MemberReq;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, columnDefinition = "bigint")
    private Long memberId;

    // 이메일은 최대 255자 + 1자(@) + 69자해서 최대 320글자이므로, varchar(320) 사용
    @Column(name = "email", nullable = false, columnDefinition = "varchar(320)")
    private String email;

    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "profile_url", columnDefinition = "text")
    private String profileUrl;

    @Column(name = "member_type", nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Column(name = "login_type", nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "status", columnDefinition = "varchar(255)")
    private String status;

    @Column(name = "major", columnDefinition = "varchar(255)")
    private String major;

    @Column(name = "job", columnDefinition = "varchar(255)")
    private String job;

    public void update(MemberReq memberReq) {
        this.name = memberReq.name();
    }

    @Builder
    public Member(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }

    @Builder
    public Member(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }
}
