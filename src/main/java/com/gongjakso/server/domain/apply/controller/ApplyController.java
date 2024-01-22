package com.gongjakso.server.domain.apply.controller;

import com.gongjakso.server.domain.apply.dto.AddApplyReq;
import com.gongjakso.server.domain.apply.dto.ApplyMemberRes;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.service.ApplyService;
import com.gongjakso.server.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/apply")
@Tag(name = "Apply", description = "팀 빌딩 관련 API")
public class ApplyController {
    private final ApplyService applyService;
    //지원 요청 api
//    @PostMapping("/1")
//    public ResponseEntity<Apply> addApply(@AuthenticationPrincipal Member member, @RequestBody AddApplyReq req){
//        Apply savedApply = applyService.save(member,req);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedApply);
//    }
    //특정 지원자 지원서 가져오는 api
//    @GetMapping("/apply/{post_id}/application")
//    public ResponseEntity<ApplyMemberRes> findMemberApplication(@RequestHeader Long member_id){
//        ApplyMemberRes applyMemberRes = applyService.findMemberApplication(member_id);
//        return ResponseEntity.ok().body(applyMemberRes);
//    }
}
