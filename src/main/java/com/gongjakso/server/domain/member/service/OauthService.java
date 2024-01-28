package com.gongjakso.server.domain.member.service;

import com.gongjakso.server.domain.member.dto.LoginRes;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.global.security.jwt.TokenProvider;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import com.gongjakso.server.global.security.kakao.KakaoClient;
import com.gongjakso.server.global.security.kakao.dto.KakaoProfile;
import com.gongjakso.server.global.security.kakao.dto.KakaoToken;
import com.gongjakso.server.global.util.redis.RedisClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private final KakaoClient kakaoClient;
    private final RedisClient redisClient;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public LoginRes signIn(String code) {
        // Business Logic
        // 카카오로 액세스 토큰 요청하기
        KakaoToken kakaoAccessToken = kakaoClient.getKakaoAccessToken(code);

        // 카카오톡에 있는 사용자 정보 반환
        KakaoProfile kakaoProfile = kakaoClient.getMemberInfo(kakaoAccessToken);

        // 반환된 정보의 이메일 기반으로 사용자 테이블에서 계정 정보 조회 진행
        // 이메일 존재 시 로그인 , 존재하지 않을 경우 회원가입 진행
        Member member = memberRepository.findMemberByEmailAndDeletedAtIsNull(kakaoProfile.kakao_account().email()).orElse(null);

        if(member == null) {
            Member newMember = Member.builder()
                    .email(kakaoProfile.kakao_account().email())
                    .name(kakaoProfile.kakao_account().profile().nickname())
                    .memberType("GENERAL")
                    .loginType("KAKAO")
                    .build();

            member = memberRepository.save(newMember);
        }

        TokenDto tokenDto = tokenProvider.createToken(member);

        // Redis에 RefreshToken 저장
        // TODO: timeout 관련되어 constant가 아닌 tokenProvider 내의 메소드로 관리할 수 있도록 수정 필요
        redisClient.setValue(member.getEmail(), tokenDto.refreshToken(), 30 * 24 * 60 * 60 * 1000L);

        // Response
        return LoginRes.of(member, tokenDto);
    }

    public void signOut(HttpServletRequest httpServletRequest) {
        // Validation

        // Business Logic

        // Response
    }

    public TokenDto reissue() {
        // Validation

        // Business Logic

        // Response
        return null;
    }
}
