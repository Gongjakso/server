package com.gongjakso.server.domain.member.controller;

import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.service.OauthService;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final OauthService oauthService;

    @Operation(summary = "로그인 API", description = "KAKAO 로그인 페이지로 리다이렉트되어 카카오 로그인을 수행할 수 있도록 안내")
    @PostMapping("/sign-in")
    public ResponseEntity<MemberRes> signIn() throws IOException {
        return oauthService.signIn();
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut() {
        return null;
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<Void> withdrawal() {
        return null;
    }

    @GetMapping("/reissue")
    public ResponseEntity<TokenDto> reissue() {
        return null;
    }
}

// https://yeees.tistory.com/231