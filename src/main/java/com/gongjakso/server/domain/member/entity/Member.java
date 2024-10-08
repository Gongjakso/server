package com.gongjakso.server.domain.member.entity;

import com.gongjakso.server.domain.member.dto.MemberReq;
import com.gongjakso.server.domain.member.enumerate.LoginType;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Table(name = "member")
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    // 이메일은 최대 255자 + 1자(@) + 69자해서 최대 320글자이므로, varchar(320) 사용
    @Column(name = "email", nullable = false, columnDefinition = "varchar(320)")
    private String email;

    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "phone", columnDefinition = "varchar(15)")
    private String phone;

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
        this.phone = memberReq.phone();
        this.status = memberReq.status();
        this.major = memberReq.major();
        this.job = memberReq.job();
    }

    @Builder
    public Member(String email, String password, String name, String phone, String profileUrl, String memberType, String loginType, String status, String major, String job) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.profileUrl = profileUrl;
        this.memberType = MemberType.valueOf(memberType);
        this.loginType = LoginType.valueOf(loginType);
        this.status = status;
        this.major = major;
        this.job = job;
    }

    public Member(Long id, String email, String password, String name, String phone, String profileUrl, String memberType, String loginType, String status, String major, String job){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.profileUrl = profileUrl;
        this.memberType = MemberType.valueOf(memberType);
        this.loginType = LoginType.valueOf(loginType);
        this.status = status;
        this.major = major;
        this.job = job;
    }
}
