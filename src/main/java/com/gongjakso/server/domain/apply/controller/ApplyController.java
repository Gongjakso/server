package com.gongjakso.server.domain.apply.controller;

import com.gongjakso.server.domain.apply.dto.AddApplyReq;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.service.ApplyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/apply")
@Tag(name = "Apply", description = "팀 빌딩 관련 API")
public class ApplyController {
    private final ApplyService applyService;
    @PostMapping("/1")
    public ResponseEntity<Apply> addApply(@RequestBody AddApplyReq req){
        Apply savedApply = applyService.save(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApply);
    }
}
