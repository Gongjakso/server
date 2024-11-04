package com.gongjakso.server.global.security.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongjakso.server.global.security.google.dto.GoogleProfile;
import com.gongjakso.server.global.security.google.dto.GoogleToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GoogleClient {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String googleGrantType;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String googleTokenUri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String googleUserInfoUri;

    /**
     *
     * @param code -
     * @return -
     */
    public GoogleToken getGoogleAccessToken(String code, String redirectUri) {
        // 요청 보낼 객체 기본 생성
        WebClient webClient = WebClient.create(googleTokenUri);

        //요청 본문
        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", googleGrantType);
        params.add("client_id", googleClientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", googleClientSecret);

        // 요청 보내기 및 응답 수신
        String response = webClient.post()
                .uri(googleTokenUri)
                .header("Content-type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData(params))
                .retrieve() // 데이터 받는 방식, 스프링에서는 exchange는 메모리 누수 가능성 때문에 retrieve 권장
                .bodyToMono(String.class) // (Mono는 단일 데이터, Flux는 복수 데이터)
                .block();// 비동기 방식의 데이터 수신

        // 수신된 응답 Mapping
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper);
        GoogleToken googleToken;
        try {
            googleToken = objectMapper.readValue(response, GoogleToken.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return googleToken;
    }

    public GoogleProfile getMemberInfo(GoogleToken googleToken) {
        // 요청 기본 객체 생성
        WebClient webClient = WebClient.create(googleUserInfoUri);

        // 요청 보내서 응답 받기
        String response = webClient.post()
                .uri(googleUserInfoUri)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + googleToken.access_token())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // 수신된 응답 Mapping
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleProfile googleProfile;
        try {
            googleProfile = objectMapper.readValue(response, GoogleProfile.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return googleProfile;
    }
}
