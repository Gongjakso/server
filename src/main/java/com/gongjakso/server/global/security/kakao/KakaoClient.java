package com.gongjakso.server.global.security.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.security.kakao.dto.KakaoMemberInfo;
import com.gongjakso.server.global.security.kakao.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoClient {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String kakaoGrantType;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoAuthorizationUri;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    public KakaoToken getKakaoAccessToken(String code) {
        // 요청 보낼 객체 기본 생성
        WebClient webClient = WebClient.builder()
                .baseUrl(kakaoAuthorizationUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE) // 헤더 설정
                .build();

        // 요청 보내기 및 응답 수신
        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", kakaoGrantType)
                        .queryParam("client_id", kakaoClientId)
                        .queryParam("client_secret", kakaoClientSecret)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", code)
                        .build())
                .retrieve() // 데이터 받는 방식, 스프링에서는 exchange는 메모리 누수 가능성 때문에 retrieve 권장
                .bodyToMono(String.class) // (Mono는 단일 데이터, Flux는 복수 데이터)
                .block();// 비동기 방식의 데이터 수신

        // 수신된 응답 Mapping
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoToken kakaoToken;
        try {
            kakaoToken = objectMapper.readValue(response, KakaoToken.class);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.KAKAO_TOKEN_EXCEPTION);
        }

        return kakaoToken;
    }
    public KakaoMemberInfo getMemberInfo(KakaoToken kakaoToken) {
        return null;
    }
}
//https://velog.io/@dab2in/Spring-boot-%EC%B9%B4%EC%B9%B4%EC%98%A4-%EC%86%8C%EC%85%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8
