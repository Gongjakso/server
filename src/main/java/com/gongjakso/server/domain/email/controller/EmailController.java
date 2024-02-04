package com.gongjakso.server.domain.email.controller;

import com.gongjakso.server.domain.email.dto.request.EmailReq;
import com.gongjakso.server.domain.email.dto.response.EmailRes;
import com.gongjakso.server.domain.email.service.EmailService;
import com.gongjakso.server.global.common.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
@Tag(name = "출시 알림 수신", description = "출시 알림 수신 관련하여, 정보 저장 및 정보 조회에 사용되는 API")
public class EmailController {

    private final EmailService emailService;

    @Operation(description = "출시 알림 수신 이메일 등록 API")
    @PostMapping("")
    public ApplicationResponse<EmailRes> registerEmail(@Valid @RequestBody EmailReq emailReq) {
        return ApplicationResponse.ok(emailService.registerEmail(emailReq));
    }
}
