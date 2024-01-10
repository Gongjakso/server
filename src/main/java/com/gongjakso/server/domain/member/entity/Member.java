package com.gongjakso.server.domain.member.entity;

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

    @Builder
    public Member(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }
}
