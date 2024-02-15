package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.post.dto.MyPageRes;
import com.gongjakso.server.domain.post.dto.PostDeleteRes;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.service.PostService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "공고 관련 API")
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ApplicationResponse<PostRes> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PostReq req) {
        return ApplicationResponse.ok(postService.create(principalDetails.getMember(), req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostRes> read(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.read(principalDetails.getMember(), id));
    }

    @PutMapping("/{id}")
    public ApplicationResponse<PostRes> modify(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id, @RequestBody PostReq req) {
        return ApplicationResponse.ok(postService.modify(principalDetails.getMember(), id, req));
    }

    @PatchMapping("/{id}")
    public ApplicationResponse<PostDeleteRes> delete(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id){
        return ApplicationResponse.ok(postService.delete(principalDetails.getMember(), id));
    }

    @GetMapping
    public ApplicationResponse<List<MyPageRes>> getMyPostList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApplicationResponse.ok(postService.getMyPostList(principalDetails.getMember()));
    }
}