package com.gongjakso.server.domain.member.controller;

import com.gongjakso.server.domain.member.dto.LoginRes;
import com.gongjakso.server.domain.member.service.OauthService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final OauthService oauthService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @Operation(summary = "로그인 API", description = "카카오 로그인 페이지로 리다이렉트되어 카카오 로그인을 수행할 수 있도록 안내")
    @PostMapping("/sign-in")
    // A0N84umtbNvrN7llZLciPDB8F4j4X9NTmC8HzbIkamDVmz9XSGbUzzj-L6sKPXVcAAABjUsXrDansOtctwzlGQ
    public ApplicationResponse<LoginRes> signIn(@RequestParam(name = "code") String code) {
        return ApplicationResponse.ok(oauthService.signIn(code));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃된 JWT 블랙리스트 등록")
    @PostMapping("/sign-out")
    public ApplicationResponse<Void> signOut(HttpServletRequest request) {
        oauthService.signOut(request);
        return ApplicationResponse.ok();
    }

    @Operation(summary = "회원탈퇴 API", description = "회원탈퇴 등록")
    @PostMapping("/withdrawal")
    public ResponseEntity<Void> withdrawal() {
        return null;
    }

    @Operation(summary = "토큰재발급 API", description = "RefreshToken 정보로 요청 시, ")
    @GetMapping("/reissue")
    public ResponseEntity<TokenDto> reissue() {
        return null;
    }
}

// https://yeees.tistory.com/231