package com.gongjakso.server.domain.member.util;

import com.gongjakso.server.domain.member.dto.MemberReq;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.LoginType;
import com.gongjakso.server.domain.member.enumerate.MemberType;

public class MemberUtilTest {

    public static Member buildMember() {
        return Member.builder()
                .email("gongjakso@google.com")
                .name("공작소")
                .phone("010-1010-1010")
                .memberType(MemberType.GENERAL.toString())
                .loginType(LoginType.KAKAO.toString())
                .status("상태")
                .major("전공")
                .job("내 미래 꿈")
                .build();
    }

    public static Member buildMemberAndId(Long id) {
        return new Member(
                id,
                "example@gmail.com",
                "password123",
                "공작소",
                "010-1234-5678",
                "http://example.com",
                MemberType.GENERAL.toString(),
                LoginType.KAKAO.toString(),
                "상태",
                "전공",
                "직업"
        );
    }

    public static MemberReq buildMemberReq() {
        return MemberReq.builder()
                .name("변경 공작소")
                .phone("010-0101-0101")
                .status("변경된 상태")
                .major("변경된 전공")
                .job("변경된 내 미래 꿈")
                .build();
    }
}
