package com.gongjakso.server.domain.member.service;

import com.gongjakso.server.domain.member.dto.LoginRes;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.LoginType;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.security.google.GoogleClient;
import com.gongjakso.server.global.security.google.dto.GoogleProfile;
import com.gongjakso.server.global.security.google.dto.GoogleToken;
import com.gongjakso.server.global.security.jwt.TokenProvider;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import com.gongjakso.server.global.security.kakao.KakaoClient;
import com.gongjakso.server.global.security.kakao.dto.KakaoProfile;
import com.gongjakso.server.global.security.kakao.dto.KakaoToken;
import com.gongjakso.server.global.security.naver.NaverClient;
import com.gongjakso.server.global.security.naver.dto.NaverProfile;
import com.gongjakso.server.global.security.naver.dto.NaverToken;
import com.gongjakso.server.global.util.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final KakaoClient kakaoClient;
    private final GoogleClient googleClient;
    private final RedisClient redisClient;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final NaverClient naverClient;

    @Transactional
    public LoginRes signIn(String code, String redirectUri, String type) {
        // Business Logic
        String loginTypeUpper = type.toUpperCase();
        LoginType loginType = LoginType.valueOf(loginTypeUpper);

        Member member = switch (loginType) {
            case KAKAO -> kakaoMember(code, redirectUri);
            case GOOGLE -> googleMember(code, redirectUri);
            case NAVER -> naverMember(code, redirectUri);
            default -> kakaoMember(code, redirectUri);
        };

        assert member != null;

        TokenDto tokenDto = tokenProvider.createToken(member);

        // Redis에 RefreshToken 저장
        // TODO: timeout 관련되어 constant가 아닌 tokenProvider 내의 메소드로 관리할 수 있도록 수정 필요
        redisClient.setValue(member.getEmail(), tokenDto.refreshToken(), 30 * 24 * 60 * 60 * 1000L);

        // Response
        return LoginRes.of(member, tokenDto);
    }

    private Member kakaoMember(String code, String redirectUri) {
        // 카카오로 액세스 토큰 요청하기
        KakaoToken kakaoAccessToken = kakaoClient.getKakaoAccessToken(code, redirectUri);

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

            return memberRepository.save(newMember);
        }
        return member;
    }

    private Member googleMember(String code, String redirectUri) {
        // 구글로 액세스 토큰 요청하기
        GoogleToken googleAccessToken = googleClient.getGoogleAccessToken(code, redirectUri);

        // 구글에 있는 사용자 정보 반환
        GoogleProfile googleProfile = googleClient.getMemberInfo(googleAccessToken);

        // 반환된 정보의 이메일 기반으로 사용자 테이블에서 계정 정보 조회 진행
        // 이메일 존재 시 로그인 , 존재하지 않을 경우 회원가입 진행
        Member member = memberRepository.findMemberByEmailAndDeletedAtIsNull(googleProfile.email()).orElse(null);

        if(member == null) {
            Member newMember = Member.builder()
                    .email(googleProfile.email())
                    .name(googleProfile.name())
                    .memberType("GENERAL")
                    .loginType("GOOGLE")
                    .build();

            return memberRepository.save(newMember);
        }
        return member;
    }

    private Member naverMember(String code, String redirectUri){
        // 네이버로 액세스 토큰 요청하기
        NaverToken naverAccessToken = naverClient.getNaverAccessToken(code, redirectUri);

        // 네이버에 있는 사용자 정보 반환
        NaverProfile naverProfile = naverClient.getMemberInfo(naverAccessToken);

        // 반환된 정보의 이메일 기반으로 사용자 테이블에서 계정 정보 조회 진행
        // 이메일 존재 시 로그인 , 존재하지 않을 경우 회원가입 진행
        Member member = memberRepository.findMemberByEmailAndDeletedAtIsNull(naverProfile.response().email()).orElse(null);

        if(member == null) {
            Member newMember = Member.builder()
                    .email(naverProfile.response().email())
                    .name(naverProfile.response().name())
                    .memberType("GENERAL")
                    .loginType("NAVER")
                    .build();

            return memberRepository.save(newMember);
        }
        return member;
    }

    public void signOut(String token, Member member) {
        // Validation
        String accessToken = token.substring(7);
        tokenProvider.validateToken(accessToken);

        // Business Logic - Refresh Token 삭제 및 Access Token 블랙리스트 등록
        String key = member.getEmail();
        redisClient.deleteValue(key);
        redisClient.setValue(accessToken, "logout", tokenProvider.getExpiration(accessToken));

        // Response
    }

    @Transactional
    public void withdrawal(Member member) {
        // Validation

        // Business Logic - 회원 논리적 삭제 진행
        memberRepository.delete(member);

        // Response
    }

    public TokenDto reissue(String token, Member member) {
        // Validation - RefreshToken 유효성 검증
        String refreshToken = token.substring(7);
        tokenProvider.validateToken(refreshToken);
        String email = tokenProvider.getEmail(refreshToken);
        String redisRefreshToken = redisClient.getValue(email);
        // 입력받은 refreshToken과 Redis의 RefreshToken 간의 일치 여부 검증
        if(refreshToken.isBlank() || redisRefreshToken.isEmpty() || !redisRefreshToken.equals(refreshToken)) {
            throw new ApplicationException(ErrorCode.WRONG_TOKEN_EXCEPTION);
        }

        // Business Logic & Response - Access Token 새로 발급 + Refresh Token의 유효 기간이 Access Token의 유효 기간보다 짧아졌을 경우 Refresh Token도 재발급
        return tokenProvider.reissue(member, refreshToken);
    }
}
