package com.gongjakso.server.domain.member.controller;

import com.gongjakso.server.domain.member.dto.MemberReq;
import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.service.MemberService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "Member", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("")
    public ApplicationResponse<MemberRes> update(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody MemberReq memberReq) {
        return ApplicationResponse.ok(memberService.update(principalDetails.getMember(), memberReq));
    }

    @GetMapping("")
    public ApplicationResponse<MemberRes> getMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApplicationResponse.ok(MemberRes.of(principalDetails.getMember()));
    }
}
