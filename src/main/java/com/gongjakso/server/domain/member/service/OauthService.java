package com.gongjakso.server.domain.member.service;

import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.global.security.kakao.KakaoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private final KakaoClient kakaoClient;
    private final MemberRepository memberRepository;

    public MemberRes signIn() {
        // Business Logic
        // 카카오로 액세스 토큰 요청하기
        String kakaoAccessToken = kakaoClient.getKakaoAccessToken();

        // 카카오톡에 있는 사용자 정보 반환


        // 반환된 정보 기반으로 로그인 또는 회원가입 진행

        // Response
        return null;
    }
}
