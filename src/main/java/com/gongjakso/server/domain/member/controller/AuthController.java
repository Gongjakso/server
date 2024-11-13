package com.gongjakso.server.domain.member.controller;

import com.gongjakso.server.domain.member.dto.LoginRes;
import com.gongjakso.server.domain.member.service.AuthService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 API", description = "카카오 로그인 페이지로 리다이렉트되어 카카오 로그인을 수행할 수 있도록 안내")
    @PostMapping("/sign-in")
    public ApplicationResponse<LoginRes> signIn(@RequestParam(name = "code") String code,
                                                @RequestParam(name = "redirect-uri") String redirectUri,
                                                @RequestParam(name = "type", required = false, defaultValue = "KAKAO") String type) {
        return ApplicationResponse.ok(authService.signIn(code, redirectUri,type));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃된 JWT 블랙리스트 등록")
    @PostMapping("/sign-out")
    public ApplicationResponse<Void> signOut(HttpServletRequest request,
                                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String token = request.getHeader("Authorization");
        authService.signOut(token, principalDetails.getMember());
        return ApplicationResponse.ok();
    }

    @Operation(summary = "회원탈퇴 API", description = "회원탈퇴 등록")
    @PostMapping("/withdrawal")
    public ApplicationResponse<Void> withdrawal(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        authService.withdrawal(principalDetails.getMember());
        return ApplicationResponse.ok();
    }

    @Operation(summary = "토큰재발급 API", description = "RefreshToken 정보로 요청 시, ")
    @GetMapping("/reissue")
    public ApplicationResponse<TokenDto> reissue(HttpServletRequest request,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String token = request.getHeader("Authorization");
        return ApplicationResponse.ok(authService.reissue(token, principalDetails.getMember()));
    }
}