package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.post.dto.CalendarRes;
import com.gongjakso.server.domain.post.service.CalendarService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
@Tag(name = "Calendar", description = "캘린더 관련 API")
public class CalendarController {
    private final CalendarService calendarService;
    @Operation(summary = "캘린더 정보 API", description = "자신이 스크랩한 공고 정보 요청")
    @GetMapping("")
    public ApplicationResponse<CalendarRes> addApply(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam(name = "year", defaultValue = "2024") int year, @RequestParam(name = "month", defaultValue = "4") int month){
        return ApplicationResponse.ok(calendarService.findScrapPost(principalDetails.getMember(),year,month));
    }
}
