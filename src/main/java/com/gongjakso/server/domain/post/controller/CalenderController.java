package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.apply.dto.ApplyReq;
import com.gongjakso.server.domain.post.dto.CalenderRes;
import com.gongjakso.server.domain.post.dto.ScrapPost;
import com.gongjakso.server.domain.post.repository.PostScrapRepository;
import com.gongjakso.server.domain.post.service.CalenderService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/calender")
@RequiredArgsConstructor
@Tag(name = "Calender", description = "캘린더 관련 API")
public class CalenderController {
    private final CalenderService calenderService;
    @Operation(summary = "캘린더 정보 API", description = "자신이 스크랩한 공고 정보 요청")
    @GetMapping("")
    public ApplicationResponse<CalenderRes> addApply(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam(name = "year", defaultValue = "2024") int year, @RequestParam(name = "month", defaultValue = "11") int month){
        return ApplicationResponse.ok(calenderService.findScrapPost(principalDetails.getMember(),year,month));
    }
}
