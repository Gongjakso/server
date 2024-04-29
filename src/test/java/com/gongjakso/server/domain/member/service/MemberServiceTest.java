package com.gongjakso.server.domain.member.service;

import com.gongjakso.server.domain.member.dto.MemberReq;
import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberUtilTest.buildMember();
    }

    @Test
    @DisplayName("사용자 정보 수정 테스트")
    void updateMemberTest() {
        // given
        MemberReq memberReq = MemberUtilTest.buildMemberReq();
        given(memberRepository.save(member)).willReturn(member);

        // when
        MemberRes memberRes = memberService.update(member, memberReq);

        // then
        assertThat(memberRes).isNotNull();
        assertThat(memberRes.name()).isEqualTo(member.getName());
        assertThat(memberRes.email()).isEqualTo(member.getEmail());
        assertThat(memberRes.phone()).isEqualTo(member.getPhone());
        assertThat(memberRes.memberType()).isEqualTo(member.getMemberType());
        assertThat(memberRes.loginType()).isEqualTo(member.getLoginType());
        assertThat(memberRes.status()).isEqualTo(member.getStatus());
        assertThat(memberRes.major()).isEqualTo(member.getMajor());
        assertThat(memberRes.job()).isEqualTo(member.getJob());
    }
}