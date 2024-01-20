package com.gongjakso.server.domain.member.controller;

import com.gongjakso.server.domain.member.dto.MemberReq;
import com.gongjakso.server.domain.member.dto.MemberRes;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "Member", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("")
    public ResponseEntity<MemberRes> update(@AuthenticationPrincipal Member member, @Valid @RequestBody MemberReq memberReq) {
        return ResponseEntity.ok(memberService.update(member, memberReq));
    }
}
