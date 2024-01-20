package com.gongjakso.server.domain.member.controller;

import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.service.AuthService;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public String testAPI() {
        return "API TEST";
    }

    @PostMapping("/sign-in")
    public ResponseEntity<MemberRes> signIn() {
        return null;
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