package com.gongjakso.server.domain.apply.controller;

import com.gongjakso.server.domain.apply.dto.AddApplyReq;
import com.gongjakso.server.domain.apply.dto.ApplyMemberRes;
import com.gongjakso.server.domain.apply.dto.OpenReq;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.service.ApplyService;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.service.PostService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/apply")
@Tag(name = "Apply", description = "팀 빌딩 관련 API")
public class ApplyController {
    private final ApplyService applyService;
    private final PostRepository postRepository;
    //지원 요청 api
    @PostMapping("/{post_id}")
    public ApplicationResponse<Void> addApply(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("post_id") Long postId, @RequestBody AddApplyReq req){
        applyService.save(principalDetails.getMember(),postId,req);
        return ApplicationResponse.created();
    }
    @PatchMapping("/{post_id}/open")
    public ResponseEntity<Apply> updateIsOpenStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("post_id") Long postId, @RequestBody OpenReq openReq){
        Post post = postRepository.findByPostId(postId);
//        if (apply == null) {
//            return ResponseEntity.notFound().build();
//        }
//        apply.setIs_open(openReq.is_open());
//        applyService.save(apply);
        return ResponseEntity.ok().build();
    }
//    특정 지원자 지원서 가져오는 api
//    @GetMapping("/apply/{post_id}/application")
//    public ResponseEntity<ApplyMemberRes> findMemberApplication(@RequestHeader Long member_id){
//        ApplyMemberRes applyMemberRes = applyService.findMemberApplication(member_id);
//        return ResponseEntity.ok().body(applyMemberRes);
//    }
}
