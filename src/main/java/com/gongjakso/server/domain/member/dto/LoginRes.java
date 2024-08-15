package com.gongjakso.server.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.LoginType;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginRes(
        @NotNull Long memberId,
        @NotNull String email,
        @NotNull String name,
        String phone,
        String profileUrl,
        MemberType memberType,
        LoginType loginType,
        String status,
        String major,
        String job,
        @NotNull String accessToken,
        @NotNull String refreshToken
) {
    public static LoginRes of(Member member, TokenDto tokenDto) {
        return LoginRes.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .profileUrl(member.getProfileUrl())
                .memberType(member.getMemberType())
                .loginType(member.getLoginType())
                .status(member.getStatus())
                .major(member.getMajor())
                .job(member.getJob())
                .accessToken(tokenDto.accessToken())
                .refreshToken(tokenDto.refreshToken())
                .build();
    }
}
