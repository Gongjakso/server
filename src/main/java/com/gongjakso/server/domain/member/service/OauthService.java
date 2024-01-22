package com.gongjakso.server.domain.member.service;

import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private KakaoOau
    private MemberRepository memberRepository;

    public MemberRes signIn() {
        String redirectUrl =
    }
}
