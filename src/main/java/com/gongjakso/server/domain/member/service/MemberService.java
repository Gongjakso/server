package com.gongjakso.server.domain.member.service;

import com.gongjakso.server.domain.member.dto.MemberReq;
import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberRes update(Member member, MemberReq memberReq) {
        // Validation

        // Business Logic
        member.update(memberReq);
        Member saveMember = memberRepository.save(member);

        // Response
        return MemberRes.of(saveMember);
    }
}
