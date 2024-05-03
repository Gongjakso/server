package com.gongjakso.server.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.LoginType;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberRes(
        @NotNull Long memberId,
        @NotNull String email,
        @NotNull String name,
        String phone,
        String profileUrl,
        MemberType memberType,
        LoginType loginType,
        String status,
        String major,
        String job
) {

    public static MemberRes of(Member member) {
        return MemberRes.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .profileUrl(member.getProfileUrl())
                .memberType(member.getMemberType())
                .loginType(member.getLoginType())
                .status(member.getStatus())
                .major(member.getMajor())
                .job(member.getJob())
                .build();
    }
}
