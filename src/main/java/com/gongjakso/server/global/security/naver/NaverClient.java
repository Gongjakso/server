package com.gongjakso.server.global.security.naver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongjakso.server.global.security.naver.dto.NaverProfile;
import com.gongjakso.server.global.security.naver.dto.NaverToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverClient {

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String naverTokenUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String naverGrantType;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String naverUserInfoUri;

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String naverAuthorizationUri;

    /**
     * 네이버 서버에 인가코드 기반으로 사용자의 토큰 정보를 조회하는 메소드
     * @param code - 네이버에서 발급해준 인가 코드
     * @return - 네이버에서 반환한 응답 토큰 객체
     */
    public NaverToken getNaverAccessToken(String code, String redirectUri) {
        // 요청 보낼 객체 기본 생성
        WebClient webClient = WebClient.create(naverTokenUri);

        //요청 본문
        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", naverGrantType);
        params.add("client_id", naverClientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", naverClientSecret);

        // 요청 보내기 및 응답 수신
        String response = webClient.post()
                .uri(naverTokenUri)
                .header("Content-type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData(params))
                .retrieve() // 데이터 받는 방식, 스프링에서는 exchange는 메모리 누수 가능성 때문에 retrieve 권장
                .bodyToMono(String.class) // (Mono는 단일 데이터, Flux는 복수 데이터)
                .block();// 비동기 방식의 데이터 수신

        // 수신된 응답 Mapping
        ObjectMapper objectMapper = new ObjectMapper();
        NaverToken naverToken;
        try {
            naverToken = objectMapper.readValue(response, NaverToken.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return naverToken;
    }

    public NaverProfile getMemberInfo(NaverToken naverToken) {
        // 요청 기본 객체 생성
        WebClient webClient = WebClient.create(naverUserInfoUri);

        // 요청 보내서 응답 받기
        String response = webClient.post()
                .uri(naverUserInfoUri)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + naverToken.access_token())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // 수신된 응답 Mapping
        ObjectMapper objectMapper = new ObjectMapper();
        NaverProfile naverProfile;
        try {
            naverProfile = objectMapper.readValue(response, NaverProfile.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return naverProfile;
    }
}
